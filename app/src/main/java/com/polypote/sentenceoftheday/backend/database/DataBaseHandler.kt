package com.polypote.sentenceoftheday.backend.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.BufferedReader
import java.io.InputStreamReader

class DataBaseHandler(val context : Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
        val inputStream = context.assets.open("DB_INSERT.sql")
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line : String? = reader.readLine()
        while (line != null){
            db?.execSQL(line)
            line = reader.readLine()
        }
        reader.close()
        inputStream.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getContentValues(citation : String, author : String) : ContentValues{
        val contentValue = ContentValues()
        contentValue.put(BODY, citation)
        contentValue.put(AUTHOR, author)
        return contentValue
    }

    fun getColumnsNamesAsArray() = arrayOf(BODY, AUTHOR)

    //Defines constants
    companion object {
        const val TABLE_NAME = "Citations"
        //Columns
        private const val ID = "id"
        private const val BODY = "body"
        private const val AUTHOR = "author"

        const val DB_NAME = "EMBEDDED_CITATION_DB"
        const val DB_VERSION = 1

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME($ID INTEGER PRIMARY KEY AUTOINCREMENT, $BODY TEXT NOT NULL, $AUTHOR TEXT);"
    }
}