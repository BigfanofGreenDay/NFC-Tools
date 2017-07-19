package cc.metapro.nfc.home

import android.content.Intent
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.metapro.nfc.R
import cc.metapro.nfc.detail.DetailActivity
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.settings.SettingsActivity
import cc.metapro.nfc.util.checkHCESupport
import cc.metapro.nfc.util.showSnackBar
import com.afollestad.materialdialogs.MaterialDialog

internal interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
}

interface ItemRemoveCallback {
    fun onRemove(card: Card)
    fun onAdd(card: Card)
}

class CardsAdapter(cardList: List<Card>) : RecyclerView.Adapter<CardsAdapter.Companion.CardViewHolder>(), ItemTouchHelperAdapter {
    private var cards: MutableList<Card> = cardList.toMutableList()
    private var mCallback: ItemTouchHelper.Callback
    private lateinit var mItemRemoveCallback: ItemRemoveCallback

    init {
        class SimpleItemTouchHelperCallback(private val mAdapter: CardsAdapter) : ItemTouchHelper.Callback() {

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.START
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val toDelete = cards[position]
                mAdapter.onItemDismiss(position)
                class ActionCallback : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        cards.add(toDelete)
                        mItemRemoveCallback.onAdd(toDelete)
                        notifyDataSetChanged()
                    }
                }
                viewHolder.itemView.showSnackBar(viewHolder.itemView.context.getString(R.string.delete_card_c, toDelete.title),
                        viewHolder.itemView.context.getString(android.R.string.cancel),
                        ActionCallback())
            }
        }
        mCallback = SimpleItemTouchHelperCallback(this)
    }

    fun getItemTouchHelperCallback(): ItemTouchHelper.Callback {
        return mCallback
    }

    fun setOnItemRemoveCallback(callback: ItemRemoveCallback) {
        mItemRemoveCallback = callback
    }

    fun setCards(cards: List<Card>) {
        this.cards = cards.toMutableList()
        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int) {
        mItemRemoveCallback.onRemove(cards[position])
        cards.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: CardViewHolder?, position: Int) {
        holder?.setView(cards[position])
        mItemRemoveCallback.onAdd(cards[position])
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
            private val mMock = itemView?.findViewById<AppCompatButton>(R.id.mock)

            fun setView(c: Card) {
                mTitleText?.text = c.title
                mDescpText?.text = c.descp
                itemView.setOnClickListener({ DetailActivity.startActivity(itemView.context, c) })
                mMock?.setOnClickListener({
                    itemView.context.checkHCESupport(MaterialDialog.SingleButtonCallback { _, _ ->
                        itemView.context.startActivity(Intent(itemView.context, SettingsActivity::class.java))
                    })
                })
            }
        }
    }
}