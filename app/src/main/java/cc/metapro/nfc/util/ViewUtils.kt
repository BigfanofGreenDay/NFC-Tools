package cc.metapro.nfc.util

import android.support.design.widget.Snackbar
import android.view.View

fun View.showSnackBar(toShow: String, actionText: String?, actionListener: View.OnClickListener?) {
    val snack = Snackbar.make(this, toShow, Snackbar.LENGTH_LONG)
    if (actionText != null) {
        snack.setAction(actionText, actionListener)
    }
    snack.show()
}