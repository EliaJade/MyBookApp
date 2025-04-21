package com.example.mybookapp.data

class MyBooks(

    var id: String,
    var status: Status,
    var title: String,
    var author: List<String>,
    var thumbnail: String?
)
{
companion object {
    const val TABLE_NAME = "MyBooks"

    const val COLUMN_BOOK_ID = "id"
    const val COLUMN_BOOK_STATUS = "status"
    const val COLUMN_BOOK_TITLE = "title"
    const val COLUMN_BOOK_AUTHOR = "author"
    const val COLUMN_BOOK_THUMBNAIL = "thumbnail"

}
}