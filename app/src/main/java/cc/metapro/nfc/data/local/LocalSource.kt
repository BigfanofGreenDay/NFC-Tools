package cc.metapro.nfc.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cc.metapro.nfc.data.Source
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.model.Category
import java.util.*

class LocalSource private constructor(context: Context) : Source {
    private val db: SQLiteDatabase = DBHelper(context).writableDatabase

    companion object {
        private lateinit var localSource: LocalSource

        fun initial(context: Context) {
            localSource = LocalSource(context)
        }

        fun getInstance(): Source {
            return localSource
        }
    }

    override fun getCards(): List<Card> {
        val c = db.rawQuery("SELECT * FROM ${CardSchema.tableName}", null)
        val result = ArrayList<Card>()
        if (c != null) {
            while (c.moveToNext()) {
                val card = CardWrapper.unWrap(c)
                result.add(card)
            }
        }
        return result
    }

    override fun getCard(id: String): Card? {
        val c = db.rawQuery("""
                |SELECT * FROM ${CardSchema.tableName}
                |WHERE ${CardSchema.cardID} = ?""".trimMargin(),
                arrayOf(id))
        if (c != null) {
            c.moveToNext()
            if (!c.isAfterLast) {
                return CardWrapper.unWrap(c)
            }
        }
        return null
    }

    override fun getCardsByCategory(category: String): List<Card> {
        val c = db.rawQuery("""
            |SELECT ${CardSchema.cardID}, ${CardSchema.cardTitle}, ${CardSchema.cardDescp}, ${CardSchema.cardData}
            |FROM ${CardCategorySchema.tableName}, ${CardSchema.tableName}, ${CategorySchema.tableName}
            |WHERE ${CardCategorySchema.cardID} = ${CardSchema.cardID}
            |   AND ${CardCategorySchema.categoryID} = ${CategorySchema.categoryID}
            |   AND ${CategorySchema.categoryName} = ?""".trimMargin(), arrayOf(category))

        val result = ArrayList<Card>()
        if (c != null) {
            while (c.moveToNext()) {
                val card = CardWrapper.unWrap(c)
                result.add(card)
            }
        }
        return result
    }

    override fun addCard(card: Card) {
        db.insert(CardSchema.tableName, null, CardWrapper.wrap(card))
    }

    override fun delCard(id: String) {
        db.delete(CardSchema.tableName, "${CardSchema.cardID} = ?", arrayOf(id))
    }

    override fun updateCard(id: String, card: Card) {
        delCard(id)
        addCard(card)
    }

    override fun getCategories(): List<Category> {
        val c = db.rawQuery("SELECT * FROM ${CategorySchema.tableName}", null)
        val result = ArrayList<Category>()
        if (c != null) {
            while (c.moveToNext()) {
                val category = CategoryWrapper.unWrap(c)
                result.add(category)
            }
        }
        return result
    }
}