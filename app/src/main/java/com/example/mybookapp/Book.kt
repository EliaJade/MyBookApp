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
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfo,
    val searchInfo: SearchInfo

){

}
    data class AccessInfo (
        val country: String,
        val viewability: String,
        val embeddable: Boolean,
        val publicDomain: Boolean,
        val textToSpeechPermission: String, //possible Boolean
        val epub: Epub,
        val pdf: Pdf,
        val webReaderLink: String,
        val accessViewStatus: String,
        val quoteSharingAllowed: Boolean
    ){

    }
        data class Epub(
            val isAvailable: Boolean
        ){
        }
        data class Pdf(
            val isAvailable: Boolean
        ){
        }

    data class SaleInfo (
        val country: String,
        val saleability: String, //possible boolean
        val isEbook: Boolean

    ){

    }
    data class SearchInfo (
        val textSnippet: String
    ){

    }
    data class VolumeInfo (
        val title: String,
        val subtitle: String? = null,
        val description: String,
        val authors: String,
        val publisher: String,
        val publishedDate: String,
        val industryIdentifiers: IndustryIdentifiers,
        val readingModes: ReadingModes,
        val pageCount: Int? = null,
        val categories: String? = null,
        val printType: String,
        val maturityRating: String, //possible boolean
        val allowAnonLogging: Boolean,
        val contentVersion: String,
        val imageLinks: String? = null,
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