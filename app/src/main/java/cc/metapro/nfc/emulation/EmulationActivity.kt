package cc.metapro.nfc.emulation

import android.content.Intent
import android.os.Bundle
import cc.metapro.nfc.R
import cc.metapro.nfc.base.BaseActivity
import cc.metapro.nfc.data.local.LocalSource
import cc.metapro.nfc.service.NFCService
import cc.metapro.nfc.util.showToast
import kotlinx.android.synthetic.main.activity_emulation.*

class EmulationActivity : BaseActivity() {

    companion object {
        val ACTION_EMULATION = "cc.metapro.nfc.EMULATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emulation)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val serviceIntent = Intent(this, NFCService::class.java)
        val id = intent.getStringExtra(NFCService.EXTRA_CARD)
        val card = LocalSource.getInstance().getCard(id)
        serviceIntent.putExtra(NFCService.EXTRA_CARD, card)
        startService(serviceIntent)

        card_emulating.setText(id)
        stop_emulation.setOnClickListener({
            stopService(serviceIntent)
            finish()
        })
    }

    override fun onBackPressed() {
        showToast(getString(R.string.press_stop_to_exit_emulation))
    }

    override fun onPause() {
        super.onPause()
        showToast(getString(R.string.emulation_exited))
        finish()
    }
}
