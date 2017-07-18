package cc.metapro.nfc.detail

import cc.metapro.nfc.base.BasePresenter
import cc.metapro.nfc.base.BaseView
import cc.metapro.nfc.model.Card

interface DetailContract {

    interface View : BaseView<Presenter> {
        fun showCard(card: Card)
        fun getResultCard(): Card
    }

    interface Presenter : BasePresenter {
        fun storeCard(card: Card)
    }
}