package cc.metapro.nfc.detail

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.metapro.nfc.R
import cc.metapro.nfc.base.ViewPagerFragment
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.util.formatAid
import cc.metapro.nfc.util.formatCardNumber
import cc.metapro.nfc.util.getResourceIdCardType
import com.github.devnied.emvnfccard.model.EmvTransactionRecord
import kotlinx.android.synthetic.main.fragment_cardinfo.*
import kotlinx.android.synthetic.main.fragment_rawinfo.*
import kotlinx.android.synthetic.main.fragment_transaction.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RawInfoFragment : ViewPagerFragment(), DetailContract.View {
    private lateinit var mPresenter: DetailContract.Presenter
    private lateinit var mCard: Card

    companion object {
        fun newInstance(): RawInfoFragment {
            val fragment = RawInfoFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_rawinfo, container, false)
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun showCard(card: Card) {
        mCard = card
    }

    override fun showWhenUserHere() {
        input_card_id?.setText(mCard.id)
        input_card_tech?.setText(mCard.tech)
        input_card_data?.setText(mCard.data)
    }

    override fun getResultCard(): Card {
        return mCard
    }

}

class BankInfoFragment : ViewPagerFragment(), DetailContract.View {
    private lateinit var mPresenter: DetailContract.Presenter
    private lateinit var mCard: Card

    companion object {
        fun newInstance(): BankInfoFragment {
            val fragment = BankInfoFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_cardinfo, container, false)
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun showCard(card: Card) {
        mCard = card
    }

    override fun showWhenUserHere() {
        card_num?.text = formatCardNumber(mCard.cardNumber, mCard.type)
        if (mCard.expireDate != null) {
            val format = SimpleDateFormat("MM/yy", Locale.getDefault())
            cardValidity.text = format.format(mCard.expireDate)
        }
        card_provider?.setImageResource(getResourceIdCardType(mCard.type))
        extended_content?.removeAllViews()
        if (!TextUtils.isEmpty(mCard.holderFirstname) && !TextUtils.isEmpty(mCard.holderLastname)) {
            createRaw(getString(R.string.holder_name), TextUtils.join(mCard.holderFirstname, arrayOf(" ", mCard.holderLastname)))
        }
        // card AID
        if (!TextUtils.isEmpty(mCard.aid)) {
            createRaw(getString(R.string.card_aid), formatAid(mCard.aid))
        }
        // Card Application label
        if (!TextUtils.isEmpty(mCard.applicationLabel)) {
            createRaw(getString(R.string.application), mCard.applicationLabel)
        }
        // Card type
        if (mCard.type != null) {
            createRaw(getString(R.string.card_type), mCard.type.getName())
        }
        // Pin try left
        createRaw(context.getString(R.string.pin_try_left), context.getString(R.string.n_times, mCard.leftPinTry))
        // Atr desc
        if (mCard.atrDescription != null && !mCard.atrDescription.isEmpty()) {
            createRaw(context.getString(R.string.card_issuer), TextUtils.join("\n", mCard.atrDescription))
        }
    }

    private fun createRaw(pKeyName: String, pValue: String) {
        val v = View.inflate(activity, R.layout.tablelayout_raw, null)
        val title = v.findViewById<TextView>(R.id.extended_raw_title)
        title.text = pKeyName
        val content = v.findViewById<TextView>(R.id.extended_raw_content)
        content.text = pValue
        extended_content.addView(v)
    }

    override fun getResultCard(): Card {
        return mCard
    }
}

class TransactionFragment : ViewPagerFragment(), DetailContract.View {
    private lateinit var mPresenter: DetailContract.Presenter
    private lateinit var mCard: Card

    private var mTransactionList = ArrayList<EmvTransactionRecord>()

    companion object {
        fun newInstance(): TransactionFragment {
            val fragment = TransactionFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_transaction, container, false)
        return view
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun showCard(card: Card) {
        mCard = card
        if (mCard.listTransactions != null) {
            mTransactionList.addAll(mCard.listTransactions)
        }
    }

    override fun showWhenUserHere() {
        recycler_view.adapter = TransactionAdapter(mTransactionList)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.itemAnimator = DefaultItemAnimator()
    }

    override fun getResultCard(): Card {
        return mCard
    }

}