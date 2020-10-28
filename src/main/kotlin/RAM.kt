import kotlin.math.*
class RAM(program: Array<Int>) {
    private val ram_addr_width = Width.DATA.value
    var table = Array(2.0.pow(ram_addr_width).toInt()) { 0 } // It should be Short but fun toInt() is deprecated
    init {
        table = program
    }
}
