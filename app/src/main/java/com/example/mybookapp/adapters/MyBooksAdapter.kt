package com.example.mybookapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mybookapp.data.MyBooks
import com.example.mybookapp.data.VolumeInfo
import com.example.mybookapp.databinding.ItemBookBinding
import com.squareup.picasso.Picasso

class MyBooksAdapter(var items: List<MyBooks>, val onClick: (Int) -> Unit) : Adapter<MyBookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBookViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size
    //return items.size



    override fun onBindViewHolder(holder: MyBookViewHolder, position: Int) {
        val book = items[position]
        holder.render(book)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }


}

class MyBookViewHolder (val binding: ItemBookBinding) : ViewHolder(binding.root){

    fun render(book: MyBooks) {
        binding.titleTextView.text = book.title
        binding.authorTextView.text = book.getAuthorsText()
        Picasso.get().load(book.thumbnail?.replace("http://", "https://")).into(binding.pictureImageView)
    }

}