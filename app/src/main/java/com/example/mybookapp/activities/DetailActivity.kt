package com.example.mybookapp.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybookapp.R
import com.example.mybookapp.data.Book
import com.example.mybookapp.data.BookService
import com.example.mybookapp.data.MyBooks
import com.example.mybookapp.data.MyBooksDAO
import com.example.mybookapp.data.Status
import com.example.mybookapp.databinding.ActivityDetailBinding
import com.example.mybookapp.utils.DatabaseManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity(){

    companion object {
        const val EXTRA_BOOK_ID = "BOOK_ID"
    }

    lateinit var binding: ActivityDetailBinding

    lateinit var book: Book

    var myBooks: MyBooks? = null
    lateinit var myBooksDAO: MyBooksDAO

    //var isSaved = false
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        getBookById(id!!)

        binding.statusChip.setOnClickListener {
            showStatusAlertDialog()
        }
    }

    fun loadData() {
        binding.nameTextView.text = book.volumeInfo.title
        binding.authorTextView.text = book.volumeInfo.getAuthorsText()
        if (book.volumeInfo.imageLinks != null) {
            Picasso.get()
                .load(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
                .into(binding.coverImageView)
        } else {
            binding.coverImageView
        }
        if(book.volumeInfo.description != null) {
            binding.bookDescriptionTextView.text = HtmlCompat.fromHtml(book.volumeInfo.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            binding.bookDescriptionTextView.text = getString(R.string.no_description)
        }

        /*if(book.volumeInfo.authors != null) {
            binding.authorTextView.text = HtmlCompat.fromHtml(book.volumeInfo.getAuthorsText()!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            binding.authorTextView.text = getString(R.string.anonymous)
        }*/


        supportActionBar?.title = book.volumeInfo.title
        supportActionBar?.subtitle = book.volumeInfo.getAuthorsText()
        loadStatus()
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
                if (myBooks != null) {
                    myBooksDAO.delete(myBooks!!)
                    myBooks = null
                    loadStatus()
                } else {
                    showStatusAlertDialog()
                }
                setSavedIcon()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }



    private fun setSavedIcon() {
        if (myBooks != null) {
            saveMenu?.setIcon(R.drawable.ic_save)
        } else {
            saveMenu?.setIcon(R.drawable.unselected_bookmark_ic)
        }
    }

    private fun loadStatus() {
        if (myBooks != null) {
            val iconId = when(myBooks!!.status) {
                Status.READ -> Status.READ.icon
                Status.READING -> Status.READING.icon
                Status.WANT_TO_READ -> Status.WANT_TO_READ.icon
            }
            binding.statusChip.setChipIconResource(iconId)
            binding.statusChip.text = getString(myBooks!!.status.title)
            binding.statusChip.visibility = View.VISIBLE

        } else {
            binding.statusChip.visibility = View.GONE
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



    fun showStatusAlertDialog() {
        val statusList = Status.entries.map { getString(it.title) }.toTypedArray()
        var status: Status = Status.WANT_TO_READ
        val checkedIndex = myBooks?.status?.ordinal ?: -1

        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.select_a_status)
        alert.setSingleChoiceItems(statusList, checkedIndex) { dialog, which ->
            status = Status.entries[which]
        }
        alert.setPositiveButton(R.string.ok) { dialog, which ->
            if (myBooks != null) {
                myBooks!!.status = status
                myBooksDAO.update(myBooks!!)
            } else {
                myBooks = MyBooks(
                    book.id,
                    status,
                    book.volumeInfo.title,
                    book.volumeInfo.authors,
                    book.volumeInfo.imageLinks?.thumbnail
                )
                myBooksDAO.insert(myBooks!!)
            }
            setSavedIcon()
            loadStatus()
        }
        alert.show()
        loadStatus()
        loadData()
    }
}

val statusNumber = 0
val status = Status.entries[statusNumber]
