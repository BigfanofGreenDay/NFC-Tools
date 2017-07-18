package cc.metapro.nfc.detail

import cc.metapro.nfc.data.local.LocalSource
import cc.metapro.nfc.model.Card

class DetailPresenter(card: Card, basicView: DetailContract.View,
                      bankView: DetailContract.View,
                      transView: DetailContract.View) : DetailContract.Presenter {

    private val mBasicView = basicView
    private val mBankView = bankView
    private val mTransView = transView
    private val mCard = card

    private val localSource = LocalSource.getInstance()

    init {
        mBankView.setPresenter(this)
        mBasicView.setPresenter(this)
        mTransView.setPresenter(this)
    }

    override fun subscribe() {
        mBankView.showCard(mCard)
        mBasicView.showCard(mCard)
        mTransView.showCard(mCard)
    }

    override fun unSubscribe() {

    }

    override fun storeCard(card: Card) {
        val testCard = localSource.getCard(card.id)
        if (testCard == null) {
            localSource.addCard(card)
        } else {
            localSource.updateCard(card.id, card)
        }
    }

}