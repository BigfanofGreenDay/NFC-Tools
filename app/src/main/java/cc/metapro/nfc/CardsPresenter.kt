package cc.metapro.nfc

import android.content.Context
import cc.metapro.nfc.data.local.LocalSource
import cc.metapro.nfc.model.Card

class CardsPresenter(context: Context, val view: CardsContract.View) : CardsContract.Presenter {

    val localSource = LocalSource.getInstance(context)

    override fun subscribe() {
        view.showCards(localSource.getCards())
    }

    override fun unSubscribe() {

    }

    override fun setCategory(category: String) {

    }

    override fun delCard(id: Int) {

    }

    override fun addCard(card: Card) {

    }
}