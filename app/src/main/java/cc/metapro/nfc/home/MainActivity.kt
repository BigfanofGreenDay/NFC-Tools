package cc.metapro.nfc.home

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cc.metapro.nfc.R
import cc.metapro.nfc.settings.SettingsActivity
import cc.metapro.nfc.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mPresenter: CardsPresenter
    private lateinit var mView: CardsFragment
    internal var nfcAdapter: NfcAdapter? = null
    internal val intentFilters = getNFCIntentFilters()
    internal lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        mView = CardsFragment.newInstance()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mView)
                    .commit()
        }

        pendingIntent = prepareNFCPendingIntent()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            showNFCNotSupport()
        } else if (!nfcAdapter!!.isEnabled) {
            showEnableNFC()
        }

        mPresenter = CardsPresenter(this, mView)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_cards -> {
            }
            R.id.nav_nfc -> startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            R.id.nav_about -> startActivity(Intent(this, SettingsActivity::class.java))
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
        mPresenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.unSubscribe()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        showToast("Received New Intent")
    }

}
