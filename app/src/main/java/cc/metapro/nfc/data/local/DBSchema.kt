package cc.metapro.nfc.data.local

import android.provider.BaseColumns

object CardSchema : BaseColumns {
    val tableName = "cards"
    val cardID = "card_id"
    val cardTitle = "card_title"
    val cardDescp = "card_descp"
    val cardKey = "card_key"
    val cardTech = "card_tech"
    val cardData = "card_data"
    val cardEmv = "card_emv"
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
            |CREATE TABLE ${CardSchema.tableName} (
            |   ${CardSchema.cardID} TEXT PRIMARY KEY,
            |   ${CardSchema.cardTitle} TEXT,
            |   ${CardSchema.cardDescp} TEXT,
            |   ${CardSchema.cardKey} TEXT,
            |   ${CardSchema.cardTech} TEXT,
            |   ${CardSchema.cardData} TEXT,
            |   ${CardSchema.cardEmv} TEXT
            |)""".trimMargin()
    val SQL_CREATE_CATEGORIES = """
            |CREATE TABLE ${CategorySchema.tableName} (
            |   ${CategorySchema.categoryID} PRIMARY KEY,
            |   ${CategorySchema.categoryName} UNIQUE NOT NULL
            |)""".trimMargin()
    val SQL_CREATE_CARD_CATEGORY = """
            |CREATE TABLE ${CardCategorySchema.tableName} (
            |   ${CardCategorySchema.cardID} TEXT,
            |   ${CardCategorySchema.categoryID} INTEGER,
            |   PRIMARY KEY (${CardCategorySchema.cardID}, ${CardCategorySchema.categoryID})
            |)""".trimMargin()
}