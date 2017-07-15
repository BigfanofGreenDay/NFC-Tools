package cc.metapro.nfc.data.local

import android.content.ContentValues
import android.database.Cursor
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.model.Category

object CardWrapper {

    fun wrap(card: Card): ContentValues {
        val c = ContentValues()
        c.put(CardSchema.cardID, card.id)
        c.put(CardSchema.cardTitle, card.title)
        c.put(CardSchema.cardDescp, card.descp)
        c.put(CardSchema.cardData, card.data)
        return c
    }

    fun unWrap(c: Cursor): Card {
        val id = c.getInt(c.getColumnIndexOrThrow(CardSchema.cardID))
        val title = c.getString(c.getColumnIndexOrThrow(CardSchema.cardTitle))
        val descp = c.getString(c.getColumnIndexOrThrow(CardSchema.cardDescp))
        val data = c.getBlob(c.getColumnIndexOrThrow(CardSchema.cardData))
        return Card(id, title, descp, data)
    }
}

object CategoryWrapper {
    fun wrap(category: Category): ContentValues {
        val c = ContentValues()
        c.put(CategorySchema.categoryID, category.id)
        c.put(CategorySchema.categoryName, category.name)
        return c
    }

    fun unWrap(c: Cursor): Category {
        val id = c.getInt(c.getColumnIndexOrThrow(CategorySchema.categoryID))
        val name = c.getString(c.getColumnIndexOrThrow(CategorySchema.categoryName))
        return Category(id, name)
    }
}