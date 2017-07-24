package cc.metapro.nfc.util

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cc.metapro.nfc.R
import com.afollestad.materialdialogs.MaterialDialog

fun Context.showToast(toShow: String) {
    Handler(Looper.getMainLooper()).post({
        Toast.makeText(this, toShow, Toast.LENGTH_LONG).show()
    })
}

fun Intent.isShareIntent(): Boolean {
    return this.action == Intent.ACTION_SEND
}

fun AppCompatActivity.showNFCNotSupport() {
    Handler(Looper.getMainLooper()).post({
        MaterialDialog.Builder(this)
                .title(R.string.nfc_not_support)
                .content(R.string.nfc_not_support_notice)
                .positiveText(android.R.string.ok).show()
    })
}

fun Context.showEnableNFC() {
    if (!getValue(PrefHelper.PREF_ENABLE_NFC_DIALOG, true)) {
        return
    }
    Handler(Looper.getMainLooper()).post({
        MaterialDialog.Builder(this)
                .title(R.string.turn_on_nfc).content(R.string.nfc_is_needed)
                .positiveText(android.R.string.ok).onPositive({ _, _ ->
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }).negativeText(android.R.string.cancel)
                .neutralText(R.string.no_more_notice).onNeutral({ _, _ ->
            putValue(PrefHelper.PREF_ENABLE_NFC_DIALOG, false)
        }).show()
    })
}