package cc.metapro.nfc.util

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog

fun Context.showToast(toShow: String) {
    Handler(Looper.getMainLooper()).post({
        Toast.makeText(this, toShow, Toast.LENGTH_LONG).show()
    })
}

fun AppCompatActivity.showNFCNotSupport() {
    Handler(Looper.getMainLooper()).post({
        MaterialDialog.Builder(this)
                .title("不支持 NFC").content("本设备不支持 NFC 功能")
                .positiveText(android.R.string.ok).onPositive({ _, _ -> finish() }).show()
    })
}

fun Context.showEnableNFC() {
    val helper = PrefHelper.getInstance(this)
    if (!helper.getBoolean(PrefHelper.PREF_ENABLE_NFC_DIALOG, true)) {
        return
    }
    Handler(Looper.getMainLooper()).post({
        MaterialDialog.Builder(this)
                .title("打开 NFC").content("需要打开 NFC 才能使用软件功能")
                .positiveText(android.R.string.ok).onPositive({ _, _ ->
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }).negativeText(android.R.string.cancel)
                .neutralText("不再提示").onNeutral({ _, _ ->
            helper.putBoolean(PrefHelper.PREF_ENABLE_NFC_DIALOG, false)
        }).show()
    })
}