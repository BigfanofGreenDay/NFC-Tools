package cc.metapro.nfc.data

import cc.metapro.nfc.model.Card
import cc.metapro.nfc.model.Category

interface Source {

    fun getCards(): List<Card>

    fun getCard(id: String): Card?

    fun getCardsByCategory(category: String): List<Card>

    fun addCard(card: Card)

    fun delCard(id: String)

    fun updateCard(id: String, card: Card)

    fun getCategories(): List<Category>

}