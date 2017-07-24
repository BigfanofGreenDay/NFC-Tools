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
            if (mCard != null) {
                val c = mCard!!
                val sb = StringBuilder(c.data)
                sb.delete(sb.indexOf("FF:FF:00:00") + 5, sb.length)
                return sb.toString().byteArray()
            }
        }
        return byteArrayOf()
    }

    override fun onDeactivated(reason: Int) {
        Log.d("Deactivated", "With reason: $reason")
    }
}