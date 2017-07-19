package cc.metapro.nfc.home

import cc.metapro.nfc.data.local.LocalSource
import cc.metapro.nfc.model.Card

class CardsPresenter(val view: CardsContract.View) : CardsContract.Presenter {

    val localSource = LocalSource.getInstance()

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
        view.showCards(localSource.getCards())
    }

    override fun unSubscribe() {

    }

    override fun setCategory(category: String) {
        view.showCards(localSource.getCardsByCategory(category))
    }

    override fun delCard(id: String) {
        localSource.delCard(id)
    }

    override fun addCard(card: Card) {
        val c = localSource.getCard(card.id)
        if (c == null) {
            localSource.addCard(card)
        }
    }

    override fun modifyCard(id: String, card: Card) {
        localSource.updateCard(id, card)
    }

    override fun storeCards(cards: List<Card>) {
        for (c in cards) {
            localSource.updateCard(c.id, c)
        }
    }

}