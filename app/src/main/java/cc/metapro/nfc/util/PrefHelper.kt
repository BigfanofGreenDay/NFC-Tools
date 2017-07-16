package cc.metapro.nfc.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PrefHelper private constructor(context: Context) {

    private val mPreference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        val PREF_ENABLE_NFC_DIALOG = "show_enable_nfc_dialog"
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

    fun putString(key: String, value: String) {
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