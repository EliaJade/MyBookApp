package com.example.mybookapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookapp.R
import com.example.mybookapp.adapters.BookAdapter
import com.example.mybookapp.adapters.MyBooksAdapter
import com.example.mybookapp.data.Book
import com.example.mybookapp.data.MyBooks
import com.example.mybookapp.data.MyBooksDAO
import com.example.mybookapp.data.Status
import com.example.mybookapp.databinding.ActivityMyBooksBinding

class MyBooksActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyBooksBinding
    lateinit var myBooksDAO: MyBooksDAO
    var myBooksList: List<MyBooks> = emptyList()

    lateinit var adapter: MyBooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMyBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "My Books"

        myBooksDAO = MyBooksDAO(this)

        adapter = MyBooksAdapter(myBooksList) { position ->
            navigateToDetail(myBooksList[position])
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        myBooksList = myBooksDAO.findAll()
        adapter.items = myBooksList
        adapter.notifyDataSetChanged()
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

    fun navigateToDetail(book: MyBooks) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_BOOK_ID, book.id)
        startActivity(intent)
    }
}