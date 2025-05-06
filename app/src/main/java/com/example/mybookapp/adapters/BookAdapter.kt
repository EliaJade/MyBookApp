package com.example.mybookapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mybookapp.R
import com.example.mybookapp.data.Book
import com.example.mybookapp.data.MyBooksDAO
import com.example.mybookapp.data.Status
import com.example.mybookapp.data.VolumeInfo
import com.example.mybookapp.databinding.ItemBookBinding
import com.squareup.picasso.Picasso

class BookAdapter(var items: List<Book>, val onClick: (Int) -> Unit) : Adapter<BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size
    //return items.size



    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = items[position]
        holder.render(book)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }


}

class BookViewHolder (val binding: ItemBookBinding) : ViewHolder(binding.root){


    fun render(book: Book) {
        binding.titleTextView.text = book.volumeInfo.title
        binding.subtitleTextView.text = book.volumeInfo.subtitle
        if (book.volumeInfo.subtitle != null) {
            binding.subtitleTextView.visibility = View.VISIBLE
        } else {
            binding.subtitleTextView.visibility = View.GONE
        }

        binding.authorTextView.text = book.volumeInfo.getAuthorsText()
        if (book.volumeInfo.imageLinks != null) {
            Picasso.get()
                .load(book.volumeInfo.imageLinks.thumbnail?.replace("http://", "https://"))
                .into(binding.pictureImageView)
        } else {
            binding.pictureImageView.visibility = View.GONE
            binding.imageErrorImageView.visibility = View.VISIBLE
            /*binding.subtitleTextView.visibility = View.GONE
            binding.titleTextView.visibility = View.GONE
            binding.authorTextView.visibility = View.GONE
            binding.pictureImageView.visibility = View.GONE
            binding.shortDescriptionTextView.visibility = View.GONE*/
        }
        loadStatus(book.id)
    }

    private fun loadStatus(id: String) {
        val context = itemView.context
        val myBooks = MyBooksDAO(context).findById(id)
        if (myBooks != null) {
            val iconId = when(myBooks.status) {
                Status.READ -> R.drawable.ic_status_read
                Status.READING -> R.drawable.ic_status_reading
                Status.WANT_TO_READ -> R.drawable.ic_status_want_to_read
            }
            binding.statusChip.setChipIconResource(iconId)
            //binding.statusChip.text = context.getString(myBooks!!.status.title)
            binding.statusChip.visibility = View.VISIBLE
        } else {
            binding.statusChip.visibility = View.GONE
        }
    }

}