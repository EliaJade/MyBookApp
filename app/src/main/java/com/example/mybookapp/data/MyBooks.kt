package com.example.mybookapp.data

class MyBooks(
    var id: String,
    var status: Status,
    var title: String,
    var authors: List<String>?,
    var thumbnail: String?
)
{
    fun getAuthorsText(): String {
        return authors?.joinToString() ?: "Anonymous"
    }

    companion object {
        const val TABLE_NAME = "MyBooks"

        const val COLUMN_BOOK_ID = "id"
        const val COLUMN_BOOK_STATUS = "status"
        const val COLUMN_BOOK_TITLE = "title"
        const val COLUMN_BOOK_AUTHOR = "author"
        const val COLUMN_BOOK_THUMBNAIL = "thumbnail"

    }
}