package com.example.mybookapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mybookapp.R
import com.example.mybookapp.data.MyBooks
import com.example.mybookapp.data.MyBooksDAO
import com.example.mybookapp.data.Status
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

    fun updateItems(Items: List<MyBooks>) {
        this.items = Items
        notifyDataSetChanged()
    }
}

class MyBookViewHolder (val binding: ItemBookBinding) : ViewHolder(binding.root){

    fun render(book: MyBooks) {
        val imageurl = book.thumbnail?.replace("http://", "https://")
        Picasso.get().load(imageurl).into(binding.pictureImageView)
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