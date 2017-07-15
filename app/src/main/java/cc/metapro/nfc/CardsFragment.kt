package cc.metapro.nfc


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.metapro.nfc.data.local.LocalSource
import cc.metapro.nfc.model.Card


/**
 * A simple [Fragment] subclass.
 * Use the [CardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardsFragment : Fragment(), CardsContract.View {

    private lateinit var mPresenter : CardsContract.Presenter
    internal lateinit var cardsAdapter: CardsAdapter
    internal lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_cards, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        cardsAdapter = CardsAdapter(ArrayList())
        recyclerView.adapter = cardsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        return view
    }

    override fun setPresenter(presenter: CardsContract.Presenter) {
        mPresenter = presenter
    }

    override fun showCards(cards: List<Card>) {
        cardsAdapter.setCards(cards)
        cardsAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): CardsFragment {
            val fragment = CardsFragment()
            return fragment
        }
    }

}
