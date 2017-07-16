package cc.metapro.nfc.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cc.metapro.nfc.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

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
