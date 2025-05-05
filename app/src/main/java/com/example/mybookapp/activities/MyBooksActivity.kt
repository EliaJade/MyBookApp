package com.example.mybookapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookapp.R
import com.example.mybookapp.adapters.BookAdapter
import com.example.mybookapp.adapters.MyBooksAdapter
import com.example.mybookapp.data.Book
import com.example.mybookapp.data.BookService
import com.example.mybookapp.data.MyBooks
import com.example.mybookapp.data.MyBooksDAO
import com.example.mybookapp.data.Status
import com.example.mybookapp.databinding.ActivityMyBooksBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyBooksActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyBooksBinding

    var filterCategory: Status? = null

    lateinit var myBooksDAO: MyBooksDAO
    var myBooksList: List<MyBooks> = emptyList()

    var filterQuery = ""

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

        supportActionBar?.title = getString(R.string.my_books)

        myBooksDAO = MyBooksDAO(this)

        adapter = MyBooksAdapter(myBooksList) { position ->
            navigateToDetail(myBooksList[position])
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)


        binding.tabBar.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position) {
                    0 -> filterCategory = null
                    1-> filterCategory = Status.WANT_TO_READ
                    2 -> filterCategory = Status.READING
                    3 -> filterCategory = Status.READ
                }
                refreshData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })
    }



    override fun onResume() {
        super.onResume()
        refreshData()

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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search_my_books, menu)

        val menuItem = menu?.findItem(R.id.action_search_my_books)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                filterQuery = query
                refreshData()
                return true
            }
        })

        return true
    }

    fun refreshData() {
        myBooksList = myBooksDAO.findByMyBookName(filterQuery,
            //filterCategory
            )
        adapter.updateItems(myBooksList)
    }

                fun navigateToDetail(book: MyBooks) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_BOOK_ID, book.id)
        startActivity(intent)
    }
}
