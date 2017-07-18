package cc.metapro.nfc.detail

import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.metapro.nfc.R
import com.github.devnied.emvnfccard.model.EmvTransactionRecord
import java.text.SimpleDateFormat
import java.util.*


class TransactionAdapter(records: List<EmvTransactionRecord>) : RecyclerView.Adapter<TransactionView>() {

    private val mTransactions = records

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TransactionView {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionView(view)
    }

    override fun getItemCount(): Int {
        return mTransactions.size
    }

    override fun onBindViewHolder(holder: TransactionView?, position: Int) {
        holder?.setupTransaction(mTransactions[position])
    }

}

class TransactionView(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val mDate = itemView.findViewById<TextInputEditText>(R.id.transaction_date)!!
    val mPrice = itemView.findViewById<TextInputEditText>(R.id.transaction_price)!!
    val mType = itemView.findViewById<TextInputEditText>(R.id.transaction_type)!!
    val mCountry = itemView.findViewById<TextInputEditText>(R.id.transaction_country)!!
    val mCrypto = itemView.findViewById<TextInputEditText>(R.id.transaction_crypto)!!

    fun setupTransaction(t: EmvTransactionRecord) {
        // 设置交易时间
        mDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(t.date))

        // 设置交易金额
        var amount = ""
        if (t.amount != null) {
            amount = t.currency.format(t.amount.toLong())
        } else {
            amount = "${t.amount}"
        }
        mPrice.setText(amount)

        // 设置交易地点
        if (t.terminalCountry != null) {
            mCountry.visibility = View.VISIBLE
            mCountry.setText(t.terminalCountry.getName())
        }

        // 设置交易加密程序信息
        if (!TextUtils.isEmpty(t.cyptogramData)) {
            mCrypto.visibility = View.VISIBLE
            mCrypto.setText(t.cyptogramData)
        }

        // 设置交易类型
        if (t.transactionType != null) {
            mType.visibility = View.VISIBLE
            mType.setText(t.transactionType.name)
        }
    }
}