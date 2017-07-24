package cc.metapro.nfc

import android.app.Application
import cc.metapro.nfc.data.local.LocalSource
import cc.metapro.nfc.util.PrefHelper

class NFC : Application() {

    override fun onCreate() {
        super.onCreate()
        initApp()
    }

    fun initApp() {
        LocalSource.initial(applicationContext)
        PrefHelper.initial(applicationContext)
    }
}