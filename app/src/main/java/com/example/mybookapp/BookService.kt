package com.example.mybookapp

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET("volumes")
    suspend fun findBookByTitle(@Query("q") query:String, @Query("key") apiKey: String = "AIzaSyDI__FYXuVm8rkkvoaGdKzJ_w72Gqc6nQs"): BookKind

    @GET("{id}")
    fun findBookById(@Path("id") id:String): Book
}