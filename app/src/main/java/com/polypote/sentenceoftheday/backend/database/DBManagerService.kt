package com.polypote.sentenceoftheday.backend.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.Calendar

class DBManagerService(val context : Context) {
    private lateinit var dbHandler: DataBaseHandler

    private lateinit var database : SQLiteDatabase

     fun open() : DBManagerService {
         dbHandler = DataBaseHandler(context)
         database = dbHandler.writableDatabase
         return this
    }

    fun close() = dbHandler.close()

    fun insert(citation : String, author : String){
        database.insert(DataBaseHandler.TABLE_NAME, null, dbHandler.getContentValues(citation, author))
    }

    @SuppressLint("Recycle")
    fun fetchForTheCurrentDay() : Cursor {
        val cursor = database.rawQuery("SELECT body, author FROM ${DataBaseHandler.TABLE_NAME} WHERE id = ?", arrayOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()))
        if(cursor == null){
            cursor?.moveToFirst()
        }
        return cursor
    }

}