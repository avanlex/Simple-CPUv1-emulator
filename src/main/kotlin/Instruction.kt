enum class Instruction(value: Byte) {
    LOAD(0b00000000.toByte()),   // Load ACC kk   : 0000 XXXX KKKKKKKK
    ADD(0b01000000.toByte()),    // Add ACC kk    : 0100 XXXX KKKKKKKK
    AND(0b00010000.toByte()),    // And ACC kk    : 0001 XXXX KKKKKKKK
    SUB(0b01100000.toByte()),    // Sub ACC kk    : 0110 XXXX KKKKKKKK
    INPUT(0b10100000.toByte()),  // Input ACC pp  : 1010 XXXX PPPPPPPP
    OUTPUT(0b11100000.toByte()), // Output ACC pp : 1110 XXXX PPPPPPPP
    JUMPU(0b10000000.toByte()),  // Jump U aa     : 1000 XXXX AAAAAAAA
    JUMPZ(0b10010000.toByte()),  // Jump Z aa     : 1001 00XX AAAAAAAA
    JUMPC(0b10011000.toByte()),  // Jump C aa     : 1001 10XX AAAAAAAA
    JUMPNZ(0b10010100.toByte()), // Jump NZ aa    : 1001 01XX AAAAAAAA
    JUMPNC(0b10011100.toByte()); // Jump NC aa    : 1001 11XX AAAAAAAA

    val opcode: Byte = value

    companion object{
        fun getName(opcode: Byte) : String{
            var retName : String = ""
            values().forEach { it -> if (opcode == it.opcode) {retName = it.name} }

            return retName
        }
    }





    /*

    In this instruction syntax X=Not used, K=Constant, A=Instruction Address, P=Data Address.
    The complexity of an instruction is also defined by its addressing mode i.e. not just how much number crunching it
    does, but how it fetches its operands (data). Again, to simplify the required hardware these instructions are
    limited to simple addressing modes:

    Immediate : operand KK, a constant value, this is immediately available as it is stored in the instruction
        register i.e. part of the instruction read during the fetch phase.
    Absolute : operand AA or PP, an address in memory. Again, the address is stored in the instruction register,
        either specifying where the data to be processed is stored i.e. an INPUT instruction, or where a result
        produced should be stored in memory i.e. an OUTPUT instruction. Also used to define the address in memory of
        the next instruction to be fetched i.e. JUMP.

    The register transfer level (RTL) description of each of these instructions is:

    Load ACC kk : ACC <- KK
    Add ACC kk : ACC <- ACC + KK
    And ACC kk : ACC <- ACC & KK
    Sub ACC kk : ACC <- ACC - KK
    Input ACC pp : ACC <- M[PP]
    Output ACC pp : M[PP] <- ACC
    Jump U aa : PC <- AA
    Jump Z aa : IF Z=1 PC <- AA ELSE PC <- PC + 1
    Jump C aa : IF C=1 PC <- AA ELSE PC <- PC + 1
    Jump NZ aa : IF Z=0 PC <- AA ELSE PC <- PC + 1
    Jump NC aa : IF C=0 PC <- AA ELSE PC <- PC + 1
    */


/*    companion object {
        var ADD = 0;
        var LOAD = 0;
        var OUTPUT = 0;
        var INPUT = 0;
        var JUMPZ = 0;
        var JUMP = 0;
        var JUMPNZ = 0;
        var JUMPC = 0;
        var JUMPNC = 0;
        var SUB = 0;
        var BITAND = 0;

        var IG = 0;
    }*/




}