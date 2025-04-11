package com.example.mybookapp.data

class MyBooks(

    var id: String,
    var status: Status
)
{
companion object {
    const val TABLE_NAME = "MyBooks"

    const val COLUMN_BOOK_ID = "id"
    const val COLUMN_BOOK_STATUS = "status"

}
}