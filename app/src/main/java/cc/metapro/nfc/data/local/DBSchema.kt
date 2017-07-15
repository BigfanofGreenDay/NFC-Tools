package cc.metapro.nfc.data.local

import android.provider.BaseColumns

object CardSchema : BaseColumns {
    val tableName = "cards"
    val cardID = "card_id"
    val cardTitle = "card_title"
    val cardDescp = "card_descp"
    val cardData = "card_data"
}

object CategorySchema : BaseColumns {
    val tableName = "categories"
    val categoryID = "cate_id"
    val categoryName = "cate_name"
}

object CardCategorySchema : BaseColumns {
    val tableName = "card_category"
    val cardID = "card_id"
    val categoryID = "category_id"
}

object Schema {
    val dbVersion = 1
    val dbName = "cards.db"

    val SQL_CREATE_CARDS = """
            |CREATE TABLE IF NOT EXISTS ${CardSchema.tableName} (
            |   ${CardSchema.cardID} INTEGER PRIMARY KEY,
            |   ${CardSchema.cardTitle} TEXT,
            |   ${CardSchema.cardDescp} TEXT,
            |   ${CardSchema.cardData}
            |)""".trimMargin()
    val SQL_CREATE_CATEGORIES = """
            |CREATE TABLE IF NOT EXISTS ${CategorySchema.tableName} (
            |   ${CategorySchema.categoryID} PRIMARY KEY,
            |   ${CategorySchema.categoryName} UNIQUE NOT NULL
            |)""".trimMargin()
    val SQL_CREATE_CARD_CATEGORY = """
            |CREATE TABLE IF NOT EXISTS ${CardCategorySchema.tableName} (
            |   ${CardCategorySchema.cardID} INTEGER,
            |   ${CardCategorySchema.categoryID} INTEGER,
            |   PRIMARY KEY (${CardCategorySchema.cardID}, ${CardCategorySchema.categoryID})
            |)""".trimMargin()
}