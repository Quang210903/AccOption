package com.example.smartlibrary1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartlibrary1.R
import com.example.smartlibrary1.data.Book
import com.example.smartlibrary1.databinding.BestDealsRvItemBinding
import com.example.smartlibrary1.databinding.ProductRvItemBinding

class BestBookAdapter: RecyclerView.Adapter<BestBookAdapter.BestBooksViewHolder>() {

    inner class BestBooksViewHolder(private val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView: TextView = itemView.findViewById(R.id.tv_name)
        fun bind(book: Book) {
            binding.apply {
                Glide.with(itemView).load(book.image).into(imgProduct)

//                tvOldPrice.text = "$ ${product.price}"
                tvName.text = book.title

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestBooksViewHolder {
        return BestBooksViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestBooksViewHolder, position: Int) {
        val book = differ.currentList[position]
        holder.bind(book)
        holder.textView.maxLines = 3
        holder.itemView.setOnClickListener {
            onClick?.invoke(book)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    var onClick: ((Book) -> Unit)? = null
}