package cc.metapro.nfc

import android.app.Application
import cc.metapro.nfc.data.local.LocalSource

class NFC : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalSource.initial(applicationContext)
//        Thread.currentThread().uncaughtExceptionHandler = ExceptionHandler()
    }
}