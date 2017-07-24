package cc.metapro.nfc.util

import android.text.TextUtils
import cc.metapro.nfc.R
import com.github.devnied.emvnfccard.enums.EmvCardScheme
import org.apache.commons.lang3.StringUtils

fun ByteArray.toCommaSepString(): String {
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format(" %02x,", b))
    }
    if (sb.isNotEmpty()) {
        sb.delete(sb.length - 1, sb.length)
    }
    return sb.toString().toUpperCase()
}

fun formatCardNumber(pCardNumber: String?, pType: EmvCardScheme?): String {
    var ret = ""
    if (!TextUtils.isEmpty(pCardNumber)) {
        // format amex
        if (pType != null && pType == EmvCardScheme.AMERICAN_EXPRESS) {
            ret = StringUtils.deleteWhitespace(pCardNumber).replaceFirst("\\d{4}", "$0 ").replaceFirst("\\d{6}", "$0 ")
                    .replaceFirst("\\d{5}", "$0").trim()
        } else {
            ret = StringUtils.deleteWhitespace(pCardNumber).replace("\\d{4}", "$0 ").trim()
        }
    } else {
        ret = "0000 0000 0000 0000"
    }
    return ret
}

fun formatAid(pAid: String): String {
    var ret = StringUtils.EMPTY
    if (StringUtils.isNotBlank(pAid)) {
        ret = StringUtils.deleteWhitespace(pAid).replace("[A-F0-9]{2}", "$0 ").trim()
    }
    return ret
}

fun getResourceIdCardType(pEnum: EmvCardScheme?): Int {
    var ret = 0
    if (pEnum != null) {
        when (pEnum) {
            EmvCardScheme.AMERICAN_EXPRESS -> ret = R.drawable.amex
            EmvCardScheme.MASTER_CARD -> ret = R.drawable.mastercard
            EmvCardScheme.VISA -> ret = R.drawable.visa
            EmvCardScheme.JCB -> ret = R.drawable.jcb
            EmvCardScheme.UNIONPAY -> ret = R.drawable.unionpay
            else -> {
            }
        }
    }
    return ret
}