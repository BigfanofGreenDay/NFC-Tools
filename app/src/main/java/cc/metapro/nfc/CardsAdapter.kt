package cc.metapro.nfc

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.metapro.nfc.model.Card

class CardsAdapter(cards: List<Card>) : RecyclerView.Adapter<CardsAdapter.Companion.CardViewHolder>() {

    private var cards: MutableList<Card> = cards.toMutableList()

    fun setCards(cards: List<Card>){
        this.cards = cards.toMutableList()
    }

    override fun onBindViewHolder(holder: CardViewHolder?, position: Int) {
        holder?.setView(cards[position])
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.nfc_card, parent, false)
        return CardViewHolder(view)
    }

    companion object {
        class CardViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            private val mTitleText = itemView?.findViewById<TextView>(R.id.card_title)
            private val mDescpText = itemView?.findViewById<TextView>(R.id.card_description)
            private val mDataText = itemView?.findViewById<TextView>(R.id.card_data)

            fun setView(c: Card) {
                mTitleText?.text = c.title
                mDescpText?.text = c.descp
                val sb = StringBuilder()
                for (b in c.data) {
                    sb.append(String.format("%02x", b)).append(" ")
                }
                mDataText?.text = sb.toString()
            }
        }
    }
}