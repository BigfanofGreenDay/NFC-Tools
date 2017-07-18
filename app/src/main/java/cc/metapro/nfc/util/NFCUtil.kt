package cc.metapro.nfc.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.util.Log
import cc.metapro.nfc.R
import cc.metapro.nfc.model.Card
import com.afollestad.materialdialogs.MaterialDialog
import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.parser.EmvParser
import com.github.devnied.emvnfccard.utils.AtrUtils
import fr.devnied.bitlib.BytesUtils
import java.io.IOException


object NFCUtil {
    val NFCFilter = arrayOf(
            NfcAdapter.ACTION_NDEF_DISCOVERED,
            NfcAdapter.ACTION_TECH_DISCOVERED,
            NfcAdapter.ACTION_TAG_DISCOVERED
    )

}

fun ByteArray.string(): String {
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format("%02x:", b))
    }
    if (sb.isNotEmpty()) {
        sb.delete(sb.length - 1, sb.length)
    }
    return sb.toString().toUpperCase()
}

private fun getAts(pIso: IsoDep): ByteArray {
    var ret: ByteArray = byteArrayOf()
    if (pIso.isConnected) {
        // Extract ATS from NFC-A
        ret = pIso.historicalBytes
        if (ret == null) {
            // Extract ATS from NFC-B
            ret = pIso.hiLayerResponse
        }
    }
    return ret
}

fun extractAtsDescription(pAts: ByteArray): Collection<String> {
    val ats = BytesUtils.bytesToString(pAts)
    return AtrUtils.getDescriptionFromAts(ats)
}

fun Context.readTag(tag: Tag): Card? {
    // card id
    val id = tag.id.string()
    val sb = StringBuilder()
    for (tech in tag.techList) {
        sb.append(tech).append('\n')
    }
    if (sb.isNotEmpty()) {
        sb.delete(sb.length - 1, sb.length)
    }
    // card tech list
    val tech = sb.toString()
    // default key is FF:FF:FF:FF:FF:FF
    val keyA = ByteArray(6, { _ -> 255.toByte() })

    var data = byteArrayOf()
    var cardEmv: EmvCard? = null
    // fetch data from the card if in detailed mode
    if (PrefHelper.getInstance(this).getBoolean(PrefHelper.PREF_DETAILED_READ_MODE, false)) {
        // resolve as EMV card
        val tagCommon = IsoDep.get(tag)
        if (tagCommon != null) {
            try {
                tagCommon.connect()
                val ats = getAts(tagCommon)
                val provider = CardProvider()
                provider.setTagCommon(tagCommon)
                val parser = EmvParser(provider, true)
                cardEmv = parser.readEmvCard()
                if (cardEmv != null) {
                    cardEmv.atrDescription = extractAtsDescription(ats)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    tagCommon.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        // Using MiFare if it is supported on the device
        val miFare = MifareClassic.get(tag)
        if (miFare != null) {
            try {
                miFare.connect()
                for (i in 0..miFare.blockCount - 1) {
                    val sectorID = miFare.blockToSector(i)
                    val ok = miFare.authenticateSectorWithKeyA(sectorID, keyA)
                    if (ok) {
                        val block = miFare.readBlock(i)
                        data += block
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    miFare.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    return Card(id, id, "Card with id $id", keyA.string(), tech, data.string(), cardEmv)
}

fun Context.handleNFCIntent(intent: Intent): Card? {
    // for NDEF message
    val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
    if (rawMessages != null) {
        // showToast("NDEF Length: ${rawMessages.size}")
    }
    // for NFC ID Card
    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
    if (tag != null) {
        return this.readTag(tag)
    }
    // for NFC IC Card
    // todo
    return null
}

fun Context.checkHCESupport(onOk: MaterialDialog.SingleButtonCallback) {
    if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
        MaterialDialog.Builder(this)
                .title(R.string.notice)
                .content(R.string.version_notice)
                .positiveText(android.R.string.ok)
                .onPositive(onOk)
                .show()
        return
    }

    MaterialDialog.Builder(this)
            .title(R.string.fail)
            .content(R.string.hce_unsupported)
            .positiveText(android.R.string.ok)
}

fun Context.prepareNFCPendingIntent(): PendingIntent {
    return PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
}

fun Intent.isNFCIntent(): Boolean {
    return NFCUtil.NFCFilter.any { this.action == it }
}

fun getNFCIntentFilters(): Array<IntentFilter> {
    val result = ArrayList<IntentFilter>(3)
    for (f in NFCUtil.NFCFilter) {
        val i = IntentFilter(f)
        try {
            i.addDataType("*/*")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            Log.d("IntentFilterInitialize", e.message)
        }
        result.add(i)
    }
    return result.toArray(arrayOf())
}