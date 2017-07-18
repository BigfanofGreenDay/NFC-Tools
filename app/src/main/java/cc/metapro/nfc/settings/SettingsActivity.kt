package cc.metapro.nfc.settings

import android.os.Bundle
import cc.metapro.nfc.R
import cc.metapro.nfc.base.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    internal lateinit var mPresenter: SettingsContract.Presenter
    internal lateinit var mView: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mView = SettingsFragment()
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, mView)
                .commit()

        mPresenter = SettingsPresenter(mView)
    }
}
