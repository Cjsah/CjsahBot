import java.util.Arrays
import kotlin.test.Test

object KotlinTest {
    @Test
    fun test() {
        val ip = "127.0.0.1"
        val port = 25565
//        println(varInt(404).contentToString())
//        println(string(ip).contentToString())
//        println(unss(port).contentToString())
//        println(varInt(1).contentToString())
//
//        val out =  packet(varInt(404) + string(ip) + unss(port) + varInt(1))
//        println(out.contentToString())

        println(packet("".toByteArray()).contentToString())
    }

    @JvmStatic
    private fun varInt(x : Int): ByteArray {
        var byte = ByteArray(0)

        var num = (x.toLong() and 0xFFFFFFFF).toInt()
        while (num and -128 > 0) {
            byte += (num and 0x7F or 0x80).toByte()
            num = num shr 7
        }
        byte += num.toByte()
        return byte
    }

    @JvmStatic
    private fun packet(data : ByteArray): ByteArray {
        val id = varInt(0) + data
        val l = varInt(id.size)
        return l + id
    }

    @JvmStatic
    private fun string(s : String): ByteArray {
        val strArr = s.toByteArray()
        return varInt(strArr.size) + strArr
    }

    @JvmStatic
    private fun unss(x : Int): ByteArray {
        var c = ByteArray(0)
        c += (x/256).toByte()
        c += (x%256).toByte()
        return c
    }

    @JvmStatic
    private fun deleteHead(array: ByteArray): ByteArray {
        var newArray = array
        while (true) {
            val index0 = newArray[0]
            newArray = newArray.copyOfRange(1,newArray.size)
            if (index0.toLong() and 0b11111111 shr 7 <= 0) {
                break
            }
        }
        return newArray
    }
}
