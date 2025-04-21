package com.example.mybookapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybookapp.R
import com.example.mybookapp.data.MyBooks
import com.example.mybookapp.data.MyBooksDAO
import com.example.mybookapp.data.Status
import com.example.mybookapp.databinding.ActivityMyBooksBinding

class MyBooksActivity : AppCompatActivity() {

    companion object {
        const val MYBOOK_ID = "MYBOOK_ID"
    }

    lateinit var binding: ActivityMyBooksBinding
    lateinit var myBooksDAO: MyBooksDAO
    lateinit var myBooks: MyBooks

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

        /*val state = Status.entries.map { }.toMutableSet()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, state)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.statusSpinner.setAdapter(adapter)*/

        val id = intent.getLongExtra(MYBOOK_ID, -1L)

        myBooksDAO = MyBooksDAO(this)

        binding.readTextView.setOnClickListener {
            //myBooks.id = id

            /*if (myBooks.id!= -1L) {
                myBooksDAO.update(myBooks)
            } else {
                myBooksDAO.insert(myBooks)
            }*/

            finish()
        }
    }
}