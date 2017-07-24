package cc.metapro.nfc.home

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.metapro.nfc.R
import cc.metapro.nfc.detail.DetailActivity
import cc.metapro.nfc.emulation.EmulationActivity
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.service.NFCService
import cc.metapro.nfc.util.showSnackBar
import cc.metapro.nfc.util.showToast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.scottyab.rootbeer.RootBeer

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
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    companion object {
        class CardViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            private val mTitleText = itemView?.findViewById<TextView>(R.id.card_title)
            private val mDescpText = itemView?.findViewById<TextView>(R.id.card_description)
            private val mEmulation = itemView?.findViewById<AppCompatButton>(R.id.emulation)
            private val mMore = itemView?.findViewById<AppCompatButton>(R.id.more)

            fun setView(c: Card) {
                mTitleText?.text = c.title
                mDescpText?.text = c.descp
                itemView.setOnClickListener({ DetailActivity.startActivity(itemView.context, c) })
                if (c.id.split(":").size > 4) {
                    mEmulation?.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                    mEmulation?.setOnClickListener { _ ->
                        itemView.context.showToast(itemView.context.getString(R.string.invalid_uid))
                    }
                } else {
                    mEmulation?.setOnClickListener {
                        val rootBeer = RootBeer(itemView.context)
                        if (!rootBeer.isRooted) {
                            itemView.context.showToast(itemView.context.getString(R.string.no_root_no_emulation))
                            return@setOnClickListener
                        } else {
                            Runtime.getRuntime().exec("su").destroy()
                            val intent = Intent(itemView.context, EmulationActivity::class.java)
                            intent.putExtra(NFCService.EXTRA_CARD, c.id)
                            itemView.context.startActivity(intent)
                        }
                    }
                }
                mMore?.setOnClickListener {
                    val dialog = MaterialDialog.Builder(itemView.context)
//                    val selectBackground = itemView.context.getString(R.string.select_background)
                    val shareCard = itemView.context.getString(R.string.share_card)
                    val items = arrayListOf(shareCard)
                    val deleteShortcut = itemView.context.getString(R.string.delete_shortcut)
                    val addShortcut = itemView.context.getString(R.string.add_shortcut)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                        val shortcutManager = itemView.context.getSystemService(ShortcutManager::class.java)
                        if (shortcutManager.dynamicShortcuts.any { it.id == c.id }) {
                            items.add(deleteShortcut)
                        } else {
                            items.add(addShortcut)
                        }
                    }

                    dialog.items(items).itemsCallback { _, _, _, text ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                            val shortcutManager = itemView.context.getSystemService(ShortcutManager::class.java)
                            when (text) {
                                deleteShortcut -> {
                                    shortcutManager.removeDynamicShortcuts(arrayListOf(c.id))
                                }
                                addShortcut -> {
                                    if (shortcutManager.dynamicShortcuts.size < 5) {
                                        val intent = Intent(EmulationActivity.ACTION_EMULATION)
                                        intent.putExtra(NFCService.EXTRA_CARD, c.id)
                                        val shortcut = ShortcutInfo.Builder(itemView.context, c.id)
                                                .setShortLabel("${itemView.context.getString(R.string.emulate)} ${c.title}")
                                                .setLongLabel("${itemView.context.getString(R.string.emulate)} ${c.title}")
                                                .setIcon(Icon.createWithResource(itemView.context, R.drawable.ic_bank_card))
                                                .setIntent(intent).build()
                                        shortcutManager.addDynamicShortcuts(arrayListOf(shortcut))
                                    } else {
                                        itemView.context.showToast(itemView.context.getString(R.string.too_many_shortcuts))
                                    }
                                }
                            }
                        }
                        when (text) {
                            shareCard -> {
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.putExtra(Intent.EXTRA_TEXT, Gson().toJson(c))
                                intent.type = "text/plain"
                                itemView.context.startActivity(intent)
                            }
//                            selectBackground -> {
//                                MaterialDialog.Builder(itemView.context)
//                                        .show()
//                            }
                        }
                    }
                    dialog.show()
                }
            }
        }
    }
}