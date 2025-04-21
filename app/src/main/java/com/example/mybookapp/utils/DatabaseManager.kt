package com.example.mybookapp.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mybookapp.data.MyBooks

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "myBooks.db"
        const val DATABASE_VERSION = 1
        private const val SQL_CREATE_MYBOOK_MYBOOKS =
            "CREATE TABLE ${MyBooks.TABLE_NAME} (" +
                    "${MyBooks.COLUMN_BOOK_ID} TEXT PRIMARY KEY," +
                    "${MyBooks.COLUMN_BOOK_STATUS} INTEGER," +
                    "${MyBooks.COLUMN_BOOK_TITLE} INTEGER," +
                    "${MyBooks.COLUMN_BOOK_AUTHOR} INTEGER," +
                    "${MyBooks.COLUMN_BOOK_THUMBNAIL} INTEGER)"

        private const val SQL_DROP_MYBOOK_MYBOOKS = "DROP TABLE IF EXISTS ${MyBooks.TABLE_NAME}"
}
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_MYBOOK_MYBOOKS)
        Log.i("DATABASE", "Created table Task")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(SQL_DROP_MYBOOK_MYBOOKS)
    }
}
