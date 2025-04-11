package com.example.mybookapp.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybookapp.R
import com.example.mybookapp.data.Book
import com.example.mybookapp.data.BookService
import com.example.mybookapp.databinding.ActivityDetailBinding
import com.example.mybookapp.databinding.ActivityMainBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity(){

    companion object {
        const val EXTRA_BOOK_ID = "BOOK_ID"
    }

    lateinit var binding: ActivityDetailBinding

    lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra("BOOK_ID")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getBookById(id!!)

    }

    fun loadData() {
        binding.nameTextView.text = book.volumeInfo.title
        binding.authorTextView.text = book.volumeInfo.getAuthorsText()
        Picasso.get().load(book.volumeInfo.imageLinks.thumbnail?.replace("http://", "https://")).into(binding.coverImageView)
        binding.bookDescriptionTextView.text = book.volumeInfo.description ?: "No description"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getBookById(id: String) {
        binding.progress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = BookService.getInstance()
                book = service.findBookById(id)


                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                    binding.progress.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
