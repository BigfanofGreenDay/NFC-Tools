package cc.metapro.nfc.home

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.cardemulation.CardEmulation
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.ToggleButton
import cc.metapro.nfc.R
import cc.metapro.nfc.base.BaseActivity
import cc.metapro.nfc.detail.DetailActivity
import cc.metapro.nfc.settings.SettingsActivity
import cc.metapro.nfc.util.*
import com.afollestad.materialdialogs.MaterialDialog
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mPresenter: CardsPresenter
    private lateinit var mView: CardsFragment
    internal var nfcAdapter: NfcAdapter? = null
    internal val intentFilters = getNFCIntentFilters()
    internal lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initView()
        mView = CardsFragment.newInstance()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mView).commit()
        }

        pendingIntent = prepareNFCPendingIntent()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        // nfc is not supported on the device
        if (nfcAdapter == null) {
            showNFCNotSupport()
        }

        // nfc is not enabled on the device
        if (!nfcAdapter!!.isEnabled) {
            showEnableNFC()
        }

        // if this activity is invoked by nfc tag, handle it
        if (intent.isNFCIntent()) {
            handleIntent(intent)
        }

        if (PrefHelper.getInstance(this).getBoolean(PrefHelper.PREF_FIRST_LAUNCH, true)) {
            MaterialDialog.Builder(this)
                    .title(R.string.welcome).content(R.string.first_launch_notice)
                    .positiveText(android.R.string.ok).show()
            PrefHelper.getInstance(this).putBoolean(PrefHelper.PREF_FIRST_LAUNCH, false)
        }

        mPresenter = CardsPresenter(mView)
    }

    fun initView() {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val toggle_detail_mode = nav_view.getHeaderView(0)
                .findViewById<ToggleButton>(R.id.toggle_detail_mode)
        toggle_detail_mode.isChecked = PrefHelper.getInstance(this)
                .getBoolean(PrefHelper.PREF_DETAILED_READ_MODE, true)
        toggle_detail_mode.setOnClickListener({ _ ->
            if (toggle_detail_mode.isChecked) {
                PrefHelper.getInstance(this).putBoolean(PrefHelper.PREF_DETAILED_READ_MODE, true)
            } else {
                PrefHelper.getInstance(this).putBoolean(PrefHelper.PREF_DETAILED_READ_MODE, false)
            }
        })
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
        if (intent != null && intent.isNFCIntent()) {
            handleIntent(intent)
        }
    }

    fun handleIntent(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CardEmulation.getInstance(nfcAdapter).getAidsForService(ComponentName("", ""), CardEmulation.CATEGORY_PAYMENT)
        }
        val progressDialog = MaterialDialog.Builder(this)
                .content(getString(R.string.card_reading_notice))
                .progress(true, 100)
                .cancelable(false)
                .build()
        progressDialog.show()
        Thread({
            val context = this
            val card = handleNFCIntent(intent)
            Handler(Looper.getMainLooper()).post({
                progressDialog.dismiss()
                if (card != null) {
                    Handler(Looper.getMainLooper()).post({
                        DetailActivity.startActivity(context, card)
                    })
                    return@post
                }

                MaterialDialog.Builder(context)
                        .title(R.string.read_fail)
                        .content(R.string.read_fail_notice)
                        .positiveText(android.R.string.ok)
                        .show()
            })
        }).start()
    }
}
