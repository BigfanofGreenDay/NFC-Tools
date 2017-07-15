package cc.metapro.nfc.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cc.metapro.nfc.data.Source
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.model.Category

class LocalSource private constructor(context: Context) : Source {

    companion object {
        internal var localSource: LocalSource? = null
        internal var db: SQLiteDatabase? = null

        fun getInstance(context: Context): Source {
            synchronized(LocalSource::class.java) {
                if (localSource == null) {
                    synchronized(LocalSource::class.java) {
                        localSource = LocalSource(context)
                    }
                }
            }
            return localSource!!
        }
    }

    init {
        val dbHelper = DBHelper(context)
        db = dbHelper.writableDatabase
    }

    override fun getCards(): List<Card> {
        val c = db?.rawQuery("SELECT * FROM ${CardSchema.tableName}", null)
        val result = ArrayList<Card>()
        if (c != null) {
            while (c.moveToNext()) {
                val card = CardWrapper.unWrap(c)
                result.add(card)
            }
        }
        result.add(Card(1, "Test", "just for test", byteArrayOf(123, 23, 56, 79, 11, 90, 12, 23, 23, 43, 53, 12, 21, 12)))
        result.add(Card(2, "Hello", "just for test", byteArrayOf()))
        result.add(Card(3, "World", "just for test", byteArrayOf()))
        return result
    }

    override fun getCard(id: Int): Card? {
        val c = db?.rawQuery("""
                |SELECT * FROM ${CardSchema.tableName}
                |WHERE ${CardSchema.cardID} = ?""".trimMargin(),
                arrayOf("{id}"))
        if (c != null && !c.isAfterLast) {
            return CardWrapper.unWrap(c)
        }
        return null
    }

    override fun getCardsByCategory(category: String): List<Card> {
        val c = db?.rawQuery("""
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
        db?.insert(CardSchema.tableName, null, CardWrapper.wrap(card))
    }

    override fun delCard(id: Int) {
        db?.delete(CardSchema.tableName, "${CardSchema.cardID} = ?", arrayOf("{id}"))
    }

    override fun updateCard(id: Int, card: Card) {
        delCard(id)
        addCard(card)
    }

    override fun getCategories(): List<Category> {
        val c = db?.rawQuery("SELECT * FROM ${CategorySchema.tableName}", null)
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