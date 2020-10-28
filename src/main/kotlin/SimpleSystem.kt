
import java.nio.ByteBuffer
import javax.swing.text.html.HTML.Attribute.N


class SimpleSystem {
    var rom: ByteArray
    var rawdata: ByteArray? = null
    val ram = Program.test_ram

    init {
        val byteBuf: ByteBuffer = ByteBuffer.allocate(2 * Program.test_ram.size)
        Program.test_ram.forEach { item -> byteBuf.putShort(item.toShort()) }
        rom = byteBuf.array()
    }

    fun readByte(address: Byte) : Byte {
        return if (address < 0x100)
            rom[address.toInt() and 0xFF]
        else
            0
    }



}