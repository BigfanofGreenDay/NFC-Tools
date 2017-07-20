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
        val PREF_CARD_TO_EMULATE = "card_to_emulate"

        private var sPref: PrefHelper? = null

        fun getInstance(context: Context): PrefHelper {
            if (sPref == null) {
                synchronized(PrefHelper::class.java) {
                    if (sPref == null) {
                        sPref = PrefHelper(context)
                    }
                }
            }
            return sPref!!
        }
    }

    fun putString(key: String, value: String): PrefHelper {
        val editor = mPreference.edit()
        editor.putString(key, value)
        editor.apply()
        return sPref!!
    }

    fun getString(key: String, defaultValue: String): String {
        return mPreference.getString(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean): PrefHelper {
        val editor = mPreference.edit()
        editor.putBoolean(key, value)
        editor.apply()
        return sPref!!
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mPreference.getBoolean(key, defaultValue)
    }
}