package cc.metapro.nfc.data

import cc.metapro.nfc.model.Card
import cc.metapro.nfc.model.Category

interface Source {

    fun getCards(): List<Card>

    fun getCard(id: Int): Card?

    fun getCardsByCategory(category: String): List<Card>

    fun addCard(card: Card)

    fun delCard(id: Int)

    fun updateCard(id: Int, card: Card)

    fun getCategories(): List<Category>

}