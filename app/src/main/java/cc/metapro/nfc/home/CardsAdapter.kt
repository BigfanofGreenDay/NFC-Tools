package cc.metapro.nfc.home

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.metapro.nfc.R
import cc.metapro.nfc.model.Card
import java.util.*

internal interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}

class CardsAdapter(cards: List<Card>) : RecyclerView.Adapter<CardsAdapter.Companion.CardViewHolder>(), ItemTouchHelperAdapter {
    private var cards: MutableList<Card> = cards.toMutableList()

    fun setCards(cards: List<Card>) {
        this.cards = cards.toMutableList()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition..toPosition - 1) {
                Collections.swap(cards, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(cards, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        cards.removeAt(position)
        notifyItemRemoved(position)
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

class SimpleItemTouchHelperCallback(private val mAdapter: CardsAdapter) : ItemTouchHelper.Callback() {

//    override fun isLongPressDragEnabled(): Boolean {
//        return true
//    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

}