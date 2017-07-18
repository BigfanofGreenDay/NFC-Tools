package cc.metapro.nfc.home

import cc.metapro.nfc.base.BasePresenter
import cc.metapro.nfc.base.BaseView
import cc.metapro.nfc.model.Card

interface CardsContract {

    interface View : BaseView<Presenter> {
        fun showCards(cards: List<Card>)
    }

    interface Presenter : BasePresenter {
        fun setCategory(category: String)
        fun storeCards(cards: List<Card>)
        fun delCard(id: String)
        fun addCard(card: Card)
        fun modifyCard(id: String, card: Card)
    }
}