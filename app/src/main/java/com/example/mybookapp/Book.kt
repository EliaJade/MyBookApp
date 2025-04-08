package com.example.mybookapp

data class BookKind(
    val kind: String,
    val items: List<Book>
){

}
data class Book (
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo
){

}
data class VolumeInfo (
    val title: String,
    val description: String,
    val authors: String,
    val publisher: String,
    val publishedDate: String,
    val industryIdentifiers: IndustryIdentifiers,
    val readingModes: ReadingModes,
    val printType: String,
    val maturityRating: String,
    val allowAnonLogging: Boolean,
    val contentVersion: String,
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String,
){

}
data class IndustryIdentifiers (
    val type: String,
    val identifier: Int,

){

}
data class ReadingModes (
    val text: Boolean,
    val image: Boolean
){

}