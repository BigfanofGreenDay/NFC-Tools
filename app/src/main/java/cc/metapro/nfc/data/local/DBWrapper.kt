package cc.metapro.nfc.data.local

import android.content.ContentValues
import android.database.Cursor
import cc.metapro.nfc.model.Card
import cc.metapro.nfc.model.Category
import com.github.devnied.emvnfccard.model.EmvCard
import com.google.gson.Gson

object CardWrapper {

    fun wrap(card: Card): ContentValues {
        val c = ContentValues()
        c.put(CardSchema.cardTitle, card.title)
        c.put(CardSchema.cardDescp, card.descp)
        c.put(CardSchema.cardID, card.id)
        c.put(CardSchema.cardKey, card.key)
        c.put(CardSchema.cardTech, card.tech)
        c.put(CardSchema.cardData, card.data)
        c.put(CardSchema.cardEmv, card.emvCard)
        return c
    }

    fun unWrap(c: Cursor): Card {
        val id = c.getString(c.getColumnIndexOrThrow(CardSchema.cardID))
        val title = c.getString(c.getColumnIndexOrThrow(CardSchema.cardTitle))
        val descp = c.getString(c.getColumnIndexOrThrow(CardSchema.cardDescp))
        val key = c.getString(c.getColumnIndexOrThrow(CardSchema.cardKey))
        val tech = c.getString(c.getColumnIndexOrThrow(CardSchema.cardTech))
        val data = c.getString(c.getColumnIndexOrThrow(CardSchema.cardData))
        val emvCard = c.getString(c.getColumnIndexOrThrow(CardSchema.cardEmv))
        val card = Gson().fromJson<EmvCard>(emvCard, EmvCard::class.java)
        return Card(id, title, descp, key, tech, data, card)
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