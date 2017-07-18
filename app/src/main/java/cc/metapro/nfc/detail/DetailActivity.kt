package cc.metapro.nfc.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.Menu
import android.view.MenuItem
import cc.metapro.nfc.R
import cc.metapro.nfc.base.BaseActivity
import cc.metapro.nfc.model.Card
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : BaseActivity() {

    private lateinit var mRawInfoView: RawInfoFragment
    private lateinit var mBankInfoView: BankInfoFragment
    private lateinit var mTransactionView: TransactionFragment
    private lateinit var mPresenter: DetailPresenter
    private lateinit var mCard: Card

    companion object {
        val KEY_CARD_EXTRA = "card_extra"
        fun startActivity(context: Context, card: Card) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_CARD_EXTRA, card)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mRawInfoView = RawInfoFragment.newInstance()
        mBankInfoView = BankInfoFragment.newInstance()
        mTransactionView = TransactionFragment.newInstance()

        tabs.setupWithViewPager(view_pager)
        class MyAdapter : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 -> return mRawInfoView
                    1 -> return mBankInfoView
                    2 -> return mTransactionView
                }
                return mRawInfoView
            }

            override fun getCount(): Int {
                return 3
            }

            override fun getPageTitle(position: Int): CharSequence {
                when (position) {
                    0 -> return getString(R.string.raw_info)
                    1 -> return getString(R.string.card_info)
                    2 -> return getString(R.string.transaction)
                }
                return super.getPageTitle(position)
            }
        }
        view_pager.adapter = MyAdapter()

        mCard = intent.getSerializableExtra(KEY_CARD_EXTRA) as Card
        mPresenter = DetailPresenter(mCard, mRawInfoView, mBankInfoView, mTransactionView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.save -> {
                mCard = mRawInfoView.getResultCard()
                mCard.title = input_card_title.text.toString()
                mCard.descp = input_card_descp.text.toString()
                mPresenter.storeCard(mCard)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        input_card_title.setText(mCard.title)
        input_card_descp.setText(mCard.descp)
        mPresenter.subscribe()
    }
}
