import java.nio.ByteBuffer
import kotlin.experimental.and

private val Byte.toBinaryString: String
    get() {return String.format("%8s", Integer.toBinaryString(this.toInt() and 0xFF)).replace(' ', '0')}

class Processor(simpleSystem: SimpleSystem) {

    var pc: Byte = 0
    var acc: Byte = 0
    var muxa: Boolean = false   // Output, ALU A input MUX control
    var muxb: Boolean = false   // ALU B input MUX control
    var muxc: Boolean = false   // Address MUX control, selecting PC or IR
    var enDa: Boolean = false   // Accumulator (ACC) register update control
    var enPc: Boolean = false   // Program counter (PC) register update control
    var enIr: Boolean = false   // Instruction register (IR) update control
    var ramWe: Boolean = false  // Memory write enable control
    var alu_s: Byte = 0        // ALU control Bus
    var ir: Short = 0           // Bus, 8bits, high byte of instruction register, contains opcode
    var zero: Boolean = false   // Driven by 8bit NOR gate connected to ALU output, if 1 indicates result is zero
    var carry: Boolean = false  // Driven by carry out (Cout) of ALU

    // TODO is it useful?
    var clk: Boolean = false    // System clock
    var ce: Boolean = false     // Clock enable, normally set to 1, if set to 0 processor will HALT
    var clr: Boolean = false    // System reset, if pulsed high system will be reset

    // how many instructions and cycles have run
    var instructionCount: Long = 0
    var cycleCount: Long = 0

    //these values are generated by decode and used in execute
    var addr = 0
    var value = 0
    var value2 = 0
    var opcode: Byte = 0
    var opcodeName: String? = null
    var operandName: String? = null

    // print out processor state for debugging?
    var doprint = false

    var system: SimpleSystem = simpleSystem

    fun mux4to1(a: Byte, b: Byte, c: Byte, d: Byte, sel: Byte): Byte {
        return when (sel) {
            0b00.toByte() -> a
            0b01.toByte() -> b
            0b10.toByte() -> c
            else -> 0xFF.toByte()
        }
    }

    // called from main loop to run a single instruction
    fun doAnInstruction() {
        // fetch instruction word to
        ir = fetch()
        // read the opcode and get the registers and memory values
        decode()
        // figure out which instruction it is and do the computation and writeback
        execute()
        // update the cycle count
        // system.docycles(cycledelay()) //6 cyc per inst, sub 1 because inst includes the fetch
        instructionCount++
        if (doprint) printState()
    }

    private fun printState() {
        //TODO("Not yet implemented")
    }

    private fun execute() {
        //TODO("Not yet implemented")
    }

    fun fetchInstruction(): Short {
        val value: Short = system.ram[pc.toInt()].toShort()
        pc = (pc + 1 and 0xff).toByte()
        return value
    }

    private fun fetch(): Short {
        val value: Short = system.ram[pc.toInt()].toShort()
        pc = (pc + 1 and 0xff).toByte()
        return value
    }

    fun increment() {
        TODO("Not yet implemented")
    }

//    private fun fetch() : Byte {
//        val value: Byte = system.readByte((PC and 0xff).toByte())
//        PC = (PC + 1 and 0xffff).toShort()
//        return value
//    }



    /*
        FETCH
        DECODE
        EXECUTE
        INCREMENT
     */
    
    private fun decode() {

        val byteBuf: ByteBuffer = ByteBuffer.allocate(2)
        byteBuf.putShort(ir)
        opcode = byteBuf[0]
        val data = byteBuf[1]

        val opString = opcode.toBinaryString
        val dataString = data.toBinaryString

        //Instruction_Decoder
        when(opcode){
            Instruction.LOAD.opcode -> acc = data

            Instruction.ADD.opcode ->      // Add ACC kk : ACC <- ACC + KK
                alu_s = 0b00000

            Instruction.AND.opcode ->      // And ACC kk : ACC <- ACC & KK
                alu_s = 0b00001

            Instruction.SUB.opcode ->      // Sub ACC kk : ACC <- ACC - KK
                alu_s = 0b01100

            Instruction.INPUT.opcode -> {  // Input ACC pp : ACC <- M[PP]
                alu_s = 0b00010 // INPUT A
                // TODO fix input
//                alu_s = 0b00011 // INPUT B
//                alu_s = 0b10000 // INPUT A
            }

            Instruction.OUTPUT.opcode ->    // Output ACC pp : M[PP] <- ACC
                alu_s = 0b00010 // OUTPUT A

            Instruction.JUMPU.opcode -> pc = data

            Instruction.JUMPZ.opcode -> pc = if (zero) data else (pc + 1).toByte()

            Instruction.JUMPC.opcode -> pc = if (carry) data else (pc + 1).toByte()

            Instruction.JUMPNZ.opcode -> pc = if (!zero) data else (pc + 1).toByte()

            Instruction.JUMPNC.opcode -> pc = if (!carry) data else (pc + 1).toByte()

            else -> {
                fault("ERROR INSTRUCTION NOT FOUND ($opString - $dataString)")
                throw Exception("EXCEPTION: INSTRUCTION NOT FOUND ($opString - $dataString)")
            }
        }

        val mem: Byte = 0 // TODO fix MEM. It must be output... or input may be 🤔
        val muxaDat = if (muxa) acc else pc
        val muxbDat = if (muxb) mem else ir and 0xff.toShort()
        val muxcDat = if (muxc) pc else ir and 0xff.toShort()
        if (opcode != Instruction.LOAD.opcode)
            acc = aluV2(acc, data)

        println("Opcode = ${"%6s".format(Instruction.getName(opcode))}, data = 0b${data.toBinaryString}, acc = 0x${"%02X".format(acc)}")
    }

    /*
      S4 S3 S2 S1 S0  Z
      0  0  0  0  0   ADD (A+B)
      0  0  0  0  1   BITWISE AND (A&B)
      0  0  0  1  0   INPUT A
      0  0  0  1  1   INPUT B
      0  1  1  0  0   SUBTRACT (A-B)
      1  0  1  0  0   INCREMENT (A+1)
      1  0  0  0  0   INPUT A
      0  0  1  0  0   ADD (A+B)+1
      0  1  0  0  0   SUBTRACT (A-B)-1
     */
    private fun aluV2(a: Byte, b: Byte): Byte {
        var aluData = 0.toByte()
        val bits = alu_s.toInt() and 0x1F
        when (bits and 0x1F) {
            0b00000 -> {
                aluData = (a + b).toByte()
            } // ADD (A+B)
            0b00001 -> {
                aluData = a and b
            } // BITWISE AND (A&B)
            0b00010 -> {
                aluData = a
            } // INPUT A
            0b00011 -> {
                aluData = b
            } // INPUT B
            0b01100 -> {
                aluData = (a.toByte() - b.toByte()).toByte() // SUBTRACT (A-B)
            }
            0b10100 -> {
                aluData = (a.toByte() + 1.toByte()).toByte() // INCREMENT (A+1)
            }
            0b10000 -> {
                aluData = a
            } // INPUT A
            0b00100 -> {
                aluData = (a.toByte() + b.toByte() + 1.toByte()) // ADD (A+B)+1
                        .toByte()
            }
            0b01000 -> {
                aluData = (a.toByte() - b.toByte() - 1.toByte()) // SUBTRACT (A-B)-1
                        .toByte()
            }
        }
        return aluData
    }

    // an invalid instruction byte was encountered
    private fun fault(message: String) {
        println(message)
//		System.exit(0);
    }

}


