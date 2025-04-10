package com.example.mybookapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mybookapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var adapter: BookAdapter
    lateinit var binding: ActivityMainBinding

    lateinit var bookList: List<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = BookAdapter(emptyList()) {

        }

        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        getRetrofit()
    }
    fun getRetrofit(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BookService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = service.findBookByTitle("flower")
                bookList = result.items

                Log.i("API", result.toString())

                CoroutineScope(Dispatchers.Main).launch {
                    adapter.items = bookList
                    adapter.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                Log.i("error en el coroutine", "Error ${e.message}")
                e.printStackTrace()
            }
        }
    }
}