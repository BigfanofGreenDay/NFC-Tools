package cc.metapro.nfc.base

interface BaseView<in T : BasePresenter> {
    fun setPresenter(presenter: T)
}

interface BasePresenter {
    fun subscribe()
    fun unSubscribe()
}