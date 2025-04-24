package com.example.mybookapp.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.mybookapp.utils.DatabaseManager

class MyBooksDAO(context: Context) {
    val databaseManager = DatabaseManager(context)

    fun insert(myBooks: MyBooks) {

        val db = databaseManager.writableDatabase

        val values = ContentValues().apply {
            put(MyBooks.COLUMN_BOOK_STATUS, myBooks.status.ordinal)
            put(MyBooks.COLUMN_BOOK_ID, myBooks.id)
            put(MyBooks.COLUMN_BOOK_TITLE, myBooks.title)
            put(MyBooks.COLUMN_BOOK_AUTHOR, myBooks.authors?.joinToString("; "))
            put(MyBooks.COLUMN_BOOK_THUMBNAIL, myBooks.thumbnail)
        }

        try {
            val newRowId = db.insert(MyBooks.TABLE_NAME, null, values)
            Log.i("DATABASE", "Inserted book with id: $newRowId")
        } catch (e:Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }
    fun update(myBooks: MyBooks) {

        val db = databaseManager.writableDatabase

        val values = ContentValues().apply {
            put(MyBooks.COLUMN_BOOK_STATUS, myBooks.status.ordinal)
            put(MyBooks.COLUMN_BOOK_ID, myBooks.id)
            put(MyBooks.COLUMN_BOOK_TITLE, myBooks.title)
            put(MyBooks.COLUMN_BOOK_AUTHOR, myBooks.authors?.joinToString("; "))
            put(MyBooks.COLUMN_BOOK_THUMBNAIL, myBooks.thumbnail)
        }
        try {
            val updatedRows  = db.update(MyBooks.TABLE_NAME, values, "${MyBooks.COLUMN_BOOK_ID} = '${myBooks.id}'", null)
            Log.i("DATABASE", "Updated book with id: $updatedRows")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }


    fun delete(myBooks: MyBooks) {
        val db = databaseManager.writableDatabase

        try {
            val deletedRows = db.delete(MyBooks.TABLE_NAME, "${MyBooks.COLUMN_BOOK_ID} = '${myBooks.id}'", null)
            Log.i("DATABASE", "Deleted book with id: $deletedRows")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }
    fun findById(id: String): MyBooks? {
        val db = databaseManager.readableDatabase
        val projection = arrayOf(
            MyBooks.COLUMN_BOOK_ID,
            MyBooks.COLUMN_BOOK_STATUS,
            MyBooks.COLUMN_BOOK_TITLE,
            MyBooks.COLUMN_BOOK_AUTHOR,
            MyBooks.COLUMN_BOOK_THUMBNAIL
        )

        val selection = "${MyBooks.COLUMN_BOOK_ID} = '$id'"

        var book: MyBooks? = null

        try {
            val cursor = db.query(
                MyBooks.TABLE_NAME,       //THE TABLE TO QUERY
                projection,         //THE ARRAY OF COLUMNS TO RETURN ( PASS NULL TO GET ALL)
                selection,          //THE COLUMNS FOR THE where CLAUSE
                null,   //THE VALUES FOR THE where CLAUSE
                null,       //DON'T GROUP THE ROWS
                null,        // DON'T FILTER BY ROW GROUPS
                null        // THE SORT ORDER
            )


            if (cursor.moveToNext()) {
                val itemId = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_ID))
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_STATUS))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_TITLE))
                val author = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_AUTHOR))?.split("; ")
                val thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_THUMBNAIL))


                book = MyBooks(itemId, Status.entries[status], title, author, thumbnail)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return book
    }

    fun findAll(): List<MyBooks> {
        val db = databaseManager.readableDatabase
        val projection = arrayOf(
            MyBooks.COLUMN_BOOK_ID,
            MyBooks.COLUMN_BOOK_STATUS,
            MyBooks.COLUMN_BOOK_TITLE,
            MyBooks.COLUMN_BOOK_AUTHOR,
            MyBooks.COLUMN_BOOK_THUMBNAIL,
        )

        val bookList: MutableList<MyBooks> = mutableListOf()

        try {
            val cursor = db.query(
                MyBooks.TABLE_NAME,       //THE TABLE TO QUERY
                projection,             //THE ARRAY OF COLUMNS TO RETURN ( PASS NULL TO GET ALL)
                null,          //THE COLUMNS FOR THE where CLAUSE
                null,       //THE VALUES FOR THE where CLAUSE
                null,           //DON'T GROUP THE ROWS
                null,            // DON'T FILTER BY ROW GROUPS
                null            // THE SORT ORDER
            )


            while (cursor.moveToNext()) {
                val itemId = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_ID))
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_STATUS))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_TITLE))
                val author = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_AUTHOR))?.split("; ")
                val thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(MyBooks.COLUMN_BOOK_THUMBNAIL))


                val book = MyBooks(itemId, Status.entries[status], title, author, thumbnail)
                bookList.add(book)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return bookList
    }
}