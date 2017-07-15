package cc.metapro.nfc.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, Schema.dbName, null, Schema.dbVersion) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Schema.SQL_CREATE_CARDS)
        db?.execSQL(Schema.SQL_CREATE_CATEGORIES)
        db?.execSQL(Schema.SQL_CREATE_CARD_CATEGORY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}