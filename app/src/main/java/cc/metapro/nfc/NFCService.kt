package cc.metapro.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle


class NFCService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray?, etras: Bundle?): ByteArray {
        return byteArrayOf()
    }

    override fun onDeactivated(reason: Int) {

    }

}
