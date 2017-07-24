package cc.metapro.nfc.emulation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import cc.metapro.nfc.R
import cc.metapro.nfc.base.BaseActivity
import cc.metapro.nfc.service.NFCService
import cc.metapro.nfc.util.*
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_emulation.*
import java.io.FileInputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter

class EmulationActivity : BaseActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mCardID: String
    private var mConfAddress: String? = null

    companion object {
        val ACTION_EMULATION = "cc.metapro.nfc.EMULATION"
        val PREV_CONF_FILE = "prev_conf.conf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emulation)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mHandler = Handler(Looper.getMainLooper())

        mCardID = intent.getStringExtra(NFCService.EXTRA_CARD)
        card_emulating.setText(mCardID)
        stop_emulation.setOnClickListener({
            finish()
        })
        initNFCConfig()
    }

    fun initNFCConfig() {
        try {
            val processes = Runtime.getRuntime()
            val su = processes.exec("su")
            val suOutput = PrintWriter(OutputStreamWriter(su.outputStream))
            suOutput.println("mount -o rw,remount /system")
            suOutput.flush()
            // find files locally
            val confFile = "nfc/${Build.DEVICE.toLowerCase().trim()}.conf"
            var conf = assets.readAll(confFile)
            val idArray = mCardID.split(":")
            conf = conf.replace("#!{COUNT}", String.format("%02x", idArray.size))
                    .replace("#!{CARD_ID}", mCardID.byteArray().toCommaSepString())
            mConfAddress = conf.substring(conf.indexOf("#") + 1, conf.indexOf("\n"))
            suOutput.println("cat << EOF > $mConfAddress")
            suOutput.println("$conf\nEOF")
            suOutput.flush()

            if (!getValue(PrefHelper.PREF_PREV_CONF_SAVED, false)) {
                // read initial configuration
                val br = FileInputStream(mConfAddress).bufferedReader()
                val writer = openFileOutput(PREV_CONF_FILE, Context.MODE_PRIVATE).bufferedWriter()
                writer.appendln(readAll(br))
                br.close()
                writer.close()
                putValue(PrefHelper.PREF_PREV_CONF_SAVED, true)
            }
            emulation_state.setText(R.string.restart_nfc_to_use)
            MaterialDialog.Builder(this)
                    .title(R.string.restart_nfc).content(R.string.nfc_config_changed)
                    .positiveText(android.R.string.ok).negativeText(android.R.string.cancel)
                    .onPositive({ _, _ ->
                        startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                    }).show()
        } catch (e: IOException) {
            emulation_state.setText(R.string.emulation_fail)
        } catch (ex: Exception) {
            emulation_state.setText(R.string.emulation_fail)
        }
    }

    @SuppressLint("SetTextI18n")
    fun appendLog(string: String) {
        mHandler.post({
            log.text = "${log.text}\n$string"
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        showToast(getString(R.string.emulation_exited))
        if (mConfAddress != null) {
            val su = Runtime.getRuntime().exec("su")
            val suOutput = PrintWriter(OutputStreamWriter(su.outputStream))
            val prevConf = readAll(openFileInput(PREV_CONF_FILE).bufferedReader())
            suOutput.println("cat << EOF > $mConfAddress")
            suOutput.println("$prevConf\nEOF")
            suOutput.flush()
            suOutput.println("mount -o ro,remount /system")
            suOutput.flush()
        }
    }
}
