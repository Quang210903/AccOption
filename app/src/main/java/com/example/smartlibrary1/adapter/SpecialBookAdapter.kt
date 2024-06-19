package com.example.smartlibrary1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartlibrary1.data.Book
import com.example.smartlibrary1.databinding.SpecialRvItemBinding
import com.example.smartlibrary1.helper.BookDatabaseHelper

class SpecialBookAdapter: RecyclerView.Adapter<SpecialBookAdapter.SpecialBooksViewHolder>() {

    inner class SpecialBooksViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(book: Book) {
            binding.apply {
                Glide.with(itemView).load(book.image).into(imageSpecialRvItem)
                tvSpecialProductName.text = book.title

//                btnAddToCart.setOnClickListener {
//                    val isInserted = db.insertBook(book.author,book.genre,book.description,book.title,book.image,book.url)
//                    if (isInserted != -1L) {
//                        Toast.makeText(binding.root.context, "Book saved", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(binding.root.context, "Failed to save book", Toast.LENGTH_SHORT).show()
//                    }
//                }
            }

        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialBooksViewHolder {
        return SpecialBooksViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialBooksViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Book) -> Unit)? = null
}