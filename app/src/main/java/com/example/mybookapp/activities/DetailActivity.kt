package com.example.mybookapp.activities

import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybookapp.R
import com.example.mybookapp.data.Book
import com.example.mybookapp.data.BookService
import com.example.mybookapp.data.MyBooks
import com.example.mybookapp.data.MyBooksDAO
import com.example.mybookapp.data.Status
import com.example.mybookapp.databinding.ActivityDetailBinding
import com.example.mybookapp.databinding.ActivityMainBinding
import com.example.mybookapp.utils.DatabaseManager
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

    var myBooks: MyBooks? = null
    lateinit var myBooksDAO: MyBooksDAO

    var isSaved = false
    var saveMenu: MenuItem? = null
    lateinit var databaseManager: DatabaseManager

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

        myBooksDAO = MyBooksDAO(this)

        val id = intent.getStringExtra("BOOK_ID")!!

        myBooks = myBooksDAO.findById(id)


        // Esto es lo que haría el botón borrar
        if (myBooks != null) {
            myBooksDAO.delete(myBooks!!)
            myBooks = null
        }

        // meter en funcion de onclicklistener (button)
        if (myBooks == null) {
            // No esta guardado
            // insert
            myBooks = MyBooks(id, Status.READ)
            myBooksDAO.insert(myBooks!!)
        } else {
            // Esta guardado con status myBooks.status
            // update
            myBooks!!.status = Status.READ
            myBooksDAO.update(myBooks!!)
        }

        // other 3 buttons are going to do an update or a insert



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getBookById(id!!)

    }

    fun loadData() {
        binding.nameTextView.text = book.volumeInfo.title
        binding.authorTextView.text = book.volumeInfo.getAuthorsText()
        Picasso.get().load(book.volumeInfo.imageLinks.thumbnail?.replace("http://", "https://")).into(binding.coverImageView)
        binding.bookDescriptionTextView.text = book.volumeInfo.description ?: "No description"
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_save, menu)

        saveMenu = menu.findItem(R.id.action_save)

        setSavedIcon()

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_save -> {
                isSaved = !isSaved
                if (isSaved) {
                    //databaseManager.onUpgrade(MyBooks.COLUMN_BOOK_STATUS)
                    //myBooksDAO.update(MyBooks.COLUMN_BOOK_STATUS)

                } else {
                    //databaseManager.onCreate(MyBooks.COLUMN_BOOK_STATUS)
                    // myBooksDAO.insert(MyBooks.COLUMN_BOOK_STATUS)

                }
                setSavedIcon()
                //println("Menu favorite")
                true

            /*R.id.action_save -> {
                val saveIntent = Intent()
                val savedIntent = Intent.createChooser(saveIntent, null)
                startActivity(savedIntent)
                true

            }*/
            else -> super.onOptionsItemSelected(item)
        }
    }
    }


    private fun setSavedIcon() {
        if (isSaved) {
            saveMenu?.setIcon(R.drawable.ic_save)

        } else {
            saveMenu?.setIcon(R.drawable.unselected_bookmark_ic)
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
