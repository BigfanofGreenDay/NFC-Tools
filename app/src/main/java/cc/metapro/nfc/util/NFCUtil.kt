package cc.metapro.nfc.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter

fun String.toApduByteArray(): ByteArray {
    val data = ByteArray(this.length / 2)
    for (i in 0..this.length - 1 step 2) {
        data[(i / 2)] = (((Character.digit(this[i], 16) shl 4) +
                Character.digit(this[i + 1], 16)).toByte())
    }
    return data
}

fun String.buildSelectApdu(): ByteArray {
    val select = "00A40400${String.format("%02X", this.length / 2)}$this"
    return select.toByteArray()
}

fun Context.prepareNFCPendingIntent(): PendingIntent {
    return PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
}

fun getNFCIntentFilters(): Array<IntentFilter> {
    val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
    val tech = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
    val tag = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
    try {
        ndef.addDataType("*/*")
        tech.addDataType("*/*")
        tag.addDataType("*/*")
    } catch (e: IntentFilter.MalformedMimeTypeException) {
        throw RuntimeException("fail", e)
    }
    return arrayOf(ndef, tech, tag)
}