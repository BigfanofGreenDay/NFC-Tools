package cc.metapro.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import cc.metapro.nfc.util.buildSelectApdu
import java.util.*


class NFCService : HostApduService() {
    private val SELECT_OK = hexStringToByteArray("1000")
    private val UNKNOWN_ERROR = hexStringToByteArray("0000")


    override fun processCommandApdu(commandApdu: ByteArray?, etras: Bundle?): ByteArray {
        val AID = "F123466666"

        // 将指令转换成 byte[]
        val selectAPDU = AID.buildSelectApdu()

        // 判断是否和读卡器发来的数据相同
        if (Arrays.equals(selectAPDU, commandApdu)) {
            // 直接模拟返回16位卡号
            val account = "6222222200000001"

            // 获取卡号 byte[]
            val accountBytes = account.toByteArray()

            // 处理欲返回的响应数据
            return concatArrays(accountBytes, SELECT_OK)
        } else {
            return UNKNOWN_ERROR
        }

    }

    override fun onDeactivated(reason: Int) {

    }

    @Throws(IllegalArgumentException::class)
    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        if (len % 2 == 1) {
            throw IllegalArgumentException("指令字符串长度必须为偶数 !!!")
        }
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    private fun concatArrays(first: ByteArray, vararg rest: ByteArray): ByteArray {
        val totalLength = first.size + rest.sumBy { it.size }
        val result = Arrays.copyOf(first, totalLength)
        var offset = first.size
        for (array in rest) {
            System.arraycopy(array, 0, result, offset, array.size)
            offset += array.size
        }
        return result
    }
}
