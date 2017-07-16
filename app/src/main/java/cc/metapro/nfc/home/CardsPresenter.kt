package cc.metapro.nfc.home

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
        view.showCards(localSource.getCardsByCategory(category))
    }

    override fun delCard(id: Int) {
        localSource.delCard(id)
    }

    override fun addCard(card: Card) {
        localSource.addCard(card)
    }

    override fun modifyCard(id: Int, card: Card) {
        localSource.updateCard(id, card)
    }

}