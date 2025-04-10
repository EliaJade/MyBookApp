package com.example.mybookapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mybookapp.data.Book
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
        holder.render(book.volumeInfo)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }


}

class BookViewHolder (val binding: ItemBookBinding) : ViewHolder(binding.root){


    fun render(volumeInfo: VolumeInfo) {
        binding.titleTextView.text = volumeInfo.title
        binding.authorTextView.text = volumeInfo.getAuthorsText()
        Picasso.get().load(volumeInfo.imageLinks.thumbnail?.replace("http://", "https://")).into(binding.pictureImageView)
    }

}