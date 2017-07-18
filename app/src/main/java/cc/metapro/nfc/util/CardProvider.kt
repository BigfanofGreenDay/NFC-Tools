package cc.metapro.nfc.util

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.exception.CommunicationException
import com.github.devnied.emvnfccard.parser.IProvider
import java.io.IOException


class CardProvider : IProvider {

    private var mTagCom: IsoDep? = null

    @Throws(CommunicationException::class)
    override fun transceive(pCommand: ByteArray): ByteArray {
        var response: ByteArray? = null
        try {
            // send command to emv card
            response = mTagCom!!.transceive(pCommand)
        } catch (e: IOException) {
            throw CommunicationException(e.message)
        }

        return response
    }

    fun setTagCommon(mTagCom: IsoDep) {
        this.mTagCom = mTagCom
    }
}