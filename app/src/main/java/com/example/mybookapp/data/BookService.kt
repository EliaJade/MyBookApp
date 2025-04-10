package com.example.mybookapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET("volumes")
    suspend fun findBookByTitle(
        @Query("q") query:String,
        @Query("startIndex") startIndex:Int = 0,
        @Query("maxResults") maxResults:Int = 40,
        @Query("key") apiKey: String = "AIzaSyDI__FYXuVm8rkkvoaGdKzJ_w72Gqc6nQs"
    ): BookKind

    @GET("author")
    suspend fun findBookByAuthor(@Query("q") query:String, @Query("key") apiKey: String = "AIzaSyDI__FYXuVm8rkkvoaGdKzJ_w72Gqc6nQs"): BookKind

    @GET("volumes/{volumeId}")
    suspend fun findBookById(@Path("volumeId") id:String): Book

    companion object {
        fun getInstance(): BookService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(BookService::class.java)
        }
    }
}