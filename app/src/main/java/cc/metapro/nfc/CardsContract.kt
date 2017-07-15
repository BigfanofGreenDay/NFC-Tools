package cc.metapro.nfc

import cc.metapro.nfc.base.BasePresenter
import cc.metapro.nfc.base.BaseView
import cc.metapro.nfc.model.Card

interface CardsContract {

    interface View : BaseView<Presenter> {
        fun showCards(cards : List<Card>)
    }

    interface Presenter :BasePresenter {
        fun setCategory(category: String)
        fun delCard(id : Int)
        fun addCard(card: Card)
    }
}