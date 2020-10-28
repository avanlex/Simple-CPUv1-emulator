import java.lang.Thread.sleep

fun main(args: Array<String>) {
    println("Simple CPUv1 emulator.")
    //val ram = RAM(Program.test_ram)
    var i = 0
    val proceccor : Processor = Processor(SimpleSystem())
    //proceccor.system.ram.forEach { proceccor.doAnInstruction()}
    //for (item in proceccor.system.ram) {
    val v = proceccor.system.ram.size
    while (i < 256){

//        print("$i  :   ")
        print(String.format("%3d ", i))
        i += 1
        proceccor.doAnInstruction()
    }
}

fun prettyBinary(input: Int, blockSize: Int, separator: String?): String? {
    val result: MutableList<String> = ArrayList()
    val binary = String.format("%16s", Integer.toBinaryString(input)).replace(' ', '0')
    var index = 0
    while (index < binary.length) {
        result.add(binary.substring(index, (index + blockSize).coerceAtMost(binary.length)))
        index += blockSize
    }
    return result.joinTo(StringBuffer(), limit = blockSize, separator = " ").toString()
}

fun prettyBinary(input: Int): String {
    val blockSize = 8
    var separator = " "
    val result: MutableList<String> = ArrayList()
    val binary = String.format("%8s", Integer.toBinaryString(input))
        .replace(' ', '0')
    var index = 0
    while (index < binary.length) {
        result.add(binary.substring(index, Math.min(index + blockSize, binary.length)))
        index += blockSize
    }
    return result.joinTo(StringBuffer(), limit = blockSize, separator = " ").toString()
}
