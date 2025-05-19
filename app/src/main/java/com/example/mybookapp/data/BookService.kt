package com.example.mybookapp.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale


interface BookService {
    @GET("volumes")
    suspend fun findBookByTitle(
        @Query("q") query:String,
        @Query("langRestrict") langRestrict:String = Locale.getDefault().toLanguageTag().split("-")[0],
        @Query("startIndex") startIndex:Int = 0,
        @Query("maxResults") maxResults:Int = 40,
        @Query("key") apiKey: String = "AIzaSyDI__FYXuVm8rkkvoaGdKzJ_w72Gqc6nQs"
    ): BookKind

    /*@GET("volumes")
    suspend fun findBookByAuthor(
        @Query("inauthor") query:String,
        @Query("startIndex") startIndex:Int = 0,
        @Query("maxResults") maxResults:Int = 40,
        @Query("key") apiKey: String = "AIzaSyDI__FYXuVm8rkkvoaGdKzJ_w72Gqc6nQs"
    ): BookKind*/

    @GET("volumes/{volumeId}")
    suspend fun findBookById(@Path("volumeId") id:String): Book

    companion object {
        fun getInstance(): BookService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(BookService::class.java)
        }
    }
}

//http://books.google.com/books/content?id=Sw1HPgAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api