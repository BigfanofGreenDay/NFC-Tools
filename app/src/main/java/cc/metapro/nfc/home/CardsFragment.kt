package cc.metapro.nfc.home


import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.metapro.nfc.R
import cc.metapro.nfc.custom.FullStandRecyclerView
import cc.metapro.nfc.model.Card


class CardsFragment : Fragment(), CardsContract.View {

    private lateinit var mPresenter: CardsContract.Presenter
    internal lateinit var mCardsAdapter: CardsAdapter
    internal lateinit var mRecyclerView: FullStandRecyclerView
    private var shortcutManager: ShortcutManager? = null

    companion object {
        fun newInstance(): CardsFragment {
            val fragment = CardsFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_allcards, container, false)
        mRecyclerView = view.findViewById<FullStandRecyclerView>(R.id.recycler_view)
        mCardsAdapter = CardsAdapter(ArrayList())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shortcutManager = context.getSystemService(ShortcutManager::class.java)
        }
        class Callback : ItemRemoveCallback {
            override fun onAdd(card: Card) {
                mPresenter.addCard(card)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    val shortcut = ShortcutInfo.Builder(context, card.id)
                            .setShortLabel("${getString(R.string.mock)} ${card.title}")
                            .setLongLabel("${getString(R.string.mock)} ${card.title}")
                            .setIcon(Icon.createWithResource(context, R.drawable.ic_bank_card))
                            .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://metapro.cc/")))
                            .build()
                    shortcutManager?.addDynamicShortcuts(arrayListOf(shortcut))
                }
            }

            override fun onRemove(card: Card) {
                mPresenter.delCard(card.id)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    shortcutManager?.removeDynamicShortcuts(arrayListOf(card.id))
                }
            }
        }
        mCardsAdapter.setOnItemRemoveCallback(Callback())
        val callback = mCardsAdapter.getItemTouchHelperCallback()
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.adapter = mCardsAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.setEmptyView(R.layout.view_empty_cards)
        return view
    }

    override fun setPresenter(presenter: CardsContract.Presenter) {
        mPresenter = presenter
    }

    override fun showCards(cards: List<Card>) {
        mCardsAdapter.setCards(cards)
        mCardsAdapter.notifyDataSetChanged()
    }

}
