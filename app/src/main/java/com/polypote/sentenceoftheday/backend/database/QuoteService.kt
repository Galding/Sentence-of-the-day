package com.polypote.sentenceoftheday.backend.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.polypote.sentenceoftheday.models.Quote
import java.util.Calendar

class QuoteService(val context : Context) : AutoCloseable {
    private var dbHandler: DataBaseHandler = DataBaseHandler(context)
    private var database : SQLiteDatabase = dbHandler.writableDatabase

    override fun close() = dbHandler.close()

    fun insert(quote: Quote){
        database.insert(DataBaseHandler.TABLE_NAME, null, dbHandler.getContentValues(quote.body, quote.author))
    }

    @SuppressLint("Recycle")
    fun fetchForTheCurrentDay() : Quote {
        val cursor = database.rawQuery("SELECT body, author FROM ${DataBaseHandler.TABLE_NAME} WHERE id = ?", arrayOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()))
        if (cursor.count == 0) {
            cursor.close()
            return Quote("No quote for today", "Unknown")
        }
        cursor.moveToFirst()
        return Quote(cursor.getString(cursor.getColumnIndex(DataBaseHandler.BODY)), cursor.getString(cursor.getColumnIndex(DataBaseHandler.AUTHOR)))
    }
}