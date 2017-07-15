package cc.metapro.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mPresenter: CardsPresenter
    private lateinit var mView: CardsFragment
    internal var nfcAdapter: NfcAdapter? = null
    internal lateinit var intentFiltersArray: Array<IntentFilter>
    internal lateinit var pendingIntent: PendingIntent
    private lateinit var detectedTag: Tag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ -> }

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

        setNFCIntentFilter()

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null || !nfcAdapter!!.isEnabled) {
            showEnableNFCDialog()
        }

        mPresenter = CardsPresenter(this, mView)
    }

    fun setNFCIntentFilter() {
        pendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val tech = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val tag = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        try {
            ndef.addDataType("*/*")
            tech.addDataType("*/*")
            tag.addDataType("*/*")
        } catch (e: MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
        intentFiltersArray = arrayOf(ndef, tech, tag)
    }

    fun showEnableNFCDialog() {
        Handler(Looper.getMainLooper()).post({
            MaterialDialog.Builder(this)
                    .title("打开 NFC").content("需要打开 NFC 才能使用软件功能")
                    .positiveText(android.R.string.ok).onPositive({ _, _ ->
                if (Build.VERSION.SDK_INT >= 16) {
                    startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                } else {
                    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                }
            }).negativeText(android.R.string.cancel).show()
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_cards -> {

            }
            R.id.nav_stared_cards -> {

            }
            R.id.nav_settings -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null)
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
        val action = intent?.action
        Toast.makeText(this, action, Toast.LENGTH_LONG).show()
        setIntent(intent)
        if (intent?.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            detectedTag = intent!!.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            readFromTag(getIntent())
        }
    }

    fun readFromTag(intent: Intent) {
        val ndef = Ndef.get(detectedTag)
        try {
            ndef.connect()
            Toast.makeText(this, "CardInfo: ${ndef.type} ${ndef.maxSize} ${ndef.isWritable}", Toast.LENGTH_LONG).show()
            val messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            if (messages != null) {
                val ndefMessages = arrayOfNulls<NdefMessage>(messages.size)
                for (i in messages.indices) {
                    ndefMessages[i] = messages[i] as NdefMessage
                }
                val record = ndefMessages[0]!!.records[0]

                val payload = record.payload
                val text = String(payload)
                Toast.makeText(this, "NFC tag value is ${text}", Toast.LENGTH_LONG).show()
                ndef.close()

            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Cannot Read From Tag.", Toast.LENGTH_LONG).show()
        }

    }

}
