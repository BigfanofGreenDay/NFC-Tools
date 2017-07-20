package cc.metapro.nfc.service

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.util.byteArray
import cc.metapro.nfc.util.string


class NFCService : HostApduService() {

    companion object {
        val EXTRA_CARD = "card_id"
    }

    private var mCard: Card? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mCard = intent?.getSerializableExtra(EXTRA_CARD) as Card?
        return super.onStartCommand(intent, flags, startId)
    }

    override fun processCommandApdu(commandApdu: ByteArray?, etras: Bundle?): ByteArray {
        if (commandApdu != null) {
            Log.d("Received APDU", commandApdu.string())
            if (commandApdu.size >= 2 && commandApdu[0] == 0.toByte() && commandApdu[1] == 0xA4.toByte()) {
                if (mCard != null) {
                    val c = mCard!!
                    Log.d("Sending response", c.id)
                    return c.id.byteArray()
                }
            } else {
                if (mCard != null) {
                    val c = mCard!!
                    val sb = StringBuilder(c.data)
                    sb.delete(0, c.id.length + 1)
                    sb.delete(128, sb.length)
                    Log.d("Sending response", sb.toString())
                    return sb.toString().byteArray()
                }
            }
        }
        return byteArrayOf()
    }

    override fun onDeactivated(reason: Int) {
        Log.d("Deactivated", "With reason: $reason")
    }

}
