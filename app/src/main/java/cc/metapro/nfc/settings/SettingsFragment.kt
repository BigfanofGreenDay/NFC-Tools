package cc.metapro.nfc.settings


import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.app.Fragment
import cc.metapro.nfc.R

class SettingsFragment : PreferenceFragment(), SettingsContract.View {

    internal lateinit var mPresenter: SettingsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }

    override fun setPresenter(presenter: SettingsContract.Presenter) {
        mPresenter = presenter
    }
}
