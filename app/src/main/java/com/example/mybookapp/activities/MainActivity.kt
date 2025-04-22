package com.example.mybookapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mybookapp.data.Book
import com.example.mybookapp.adapters.BookAdapter
import com.example.mybookapp.data.BookService
import com.example.mybookapp.R
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
        supportActionBar?.title = "Library"

        adapter = BookAdapter(emptyList()) { position ->
            navigateToDetail(bookList[position])
        }



        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)

        searchBookByName("a")

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)

        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Log.i("MENU", "I pressed Enter")
                searchBookByName(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save -> {
                val intent = Intent(this, MyBooksActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun searchBookByName(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = BookService.getInstance()
                val result = service.findBookByTitle(query)
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

    fun navigateToDetail(book: Book) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_BOOK_ID, book.id)
        startActivity(intent)
    }
}