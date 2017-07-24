package cc.metapro.nfc.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PrefHelper private constructor(context: Context) {

    private val mPreference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        val PREF_ENABLE_NFC_DIALOG = "show_enable_nfc_dialog"
        val PREF_DETAILED_READ_MODE = "detailed_read_mode"
        val PREF_FIRST_LAUNCH = "first_launch"
        val PREF_PREV_CONF_SAVED = "prev_conf_saved"
        val PREF_FIRST_EMULATION = "first_emulation"

        private lateinit var sPref: PrefHelper

        fun initial(context: Context) {
            sPref = PrefHelper(context)
        }

        fun getInstance(): PrefHelper {
            return sPref
        }
    }

    internal inline fun <reified T> getValue(key: String, defaultValue: T): T {
        when (defaultValue) {
            is String -> return mPreference.getString(key, defaultValue) as T
            is Boolean -> return mPreference.getBoolean(key, defaultValue) as T
            is Int -> return mPreference.getInt(key, defaultValue) as T
            is Long -> return mPreference.getLong(key, defaultValue) as T
            is Float -> return mPreference.getFloat(key, defaultValue) as T
            else -> throw UnsupportedOperationException("Unknown value type")
        }
    }

    internal inline fun <reified T> putValue(key: String, value: T): PrefHelper {
        val editor = mPreference.edit()
        try {
            when (value) {
                is String -> editor.putString(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Int -> editor.putInt(key, value)
                is Long -> editor.putLong(key, value)
                is Float -> editor.putFloat(key, value)
                else -> throw UnsupportedOperationException("Unknown value type")
            }
        } finally {
            editor.apply()
        }
        return sPref
    }
}

internal inline fun <reified T> getValue(key: String, defaultValue: T): T {
    return PrefHelper.getInstance().getValue(key, defaultValue)
}

internal inline fun <reified T> putValue(key: String, value: T) {
    PrefHelper.getInstance().putValue(key, value)
}