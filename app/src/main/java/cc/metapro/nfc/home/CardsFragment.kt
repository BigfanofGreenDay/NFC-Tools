package cc.metapro.nfc.home


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
        class Callback : ItemRemoveCallback {
            override fun onAdd(card: Card) {
                mPresenter.addCard(card)
            }

            override fun onRemove(card: Card) {
                mPresenter.delCard(card.id)
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
