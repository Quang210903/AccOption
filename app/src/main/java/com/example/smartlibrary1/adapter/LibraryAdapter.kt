package com.example.smartlibrary1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Intent
import android.widget.Toast
import com.example.smartlibrary1.data.BookDetail
import com.example.smartlibrary1.databinding.FragmentLibraryBookDetailBinding
import com.example.smartlibrary1.databinding.LibraryBookItemBinding
import com.example.smartlibrary1.fragments.library.Add_Plan
import com.example.smartlibrary1.fragments.library.NoteBook
import com.example.smartlibrary1.helper.BookDatabaseHelper

class LibraryAdapter(private val context: Context, private val books: MutableList<BookDetail>, private val db: BookDatabaseHelper): RecyclerView.Adapter<LibraryAdapter.LibraryBooksViewHolder>() {

    //private lateinit var binding: FragmentLibraryBookDetailBinding
    //private lateinit var bookDetail1: BookDetail
    //private lateinit var fragmentContext: Context

    inner class LibraryBooksViewHolder(val binding: LibraryBookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookDetail: BookDetail) {
            binding.apply {
                Glide.with(itemView).load(bookDetail.image).into(imageLibraryBook)
                tvLibraryBook.text = bookDetail.title
                tvLibraryBookAuthor.text = bookDetail.author
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<BookDetail>() {
        override fun areItemsTheSame(oldItem: BookDetail, newItem: BookDetail): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: BookDetail, newItem: BookDetail): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryBooksViewHolder {
        return LibraryBooksViewHolder(
            LibraryBookItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LibraryBooksViewHolder, position: Int) {
        val book = differ.currentList[position]
        holder.bind(book)

        holder.binding.deleteBtn.setOnClickListener {
            val id = db.getIdBookByTitle(book.title)
            db.deleteNoteByBookId(id!!)
            db.deletePlanByBookId(id)
            db.deleteBookById(id)

            books.removeAt(position)
            differ.submitList(books.toList()) {
                if (books.isEmpty()) {
                    notifyEmptyList()
                }
                notifyDataSetChanged()  // Ensure RecyclerView updates correctly
            }
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(book)
        }

        holder.binding.addNoteBtn.setOnClickListener {
            val id = db.getIdBookByTitle(book.title)
            val intent = Intent(holder.itemView.context, NoteBook::class.java)
            intent.putExtra("idbook", id)
            holder.itemView.context.startActivity(intent)
        }

        holder.binding.addPlanBtn.setOnClickListener {
            val id = db.getIdBookByTitle(book.title)
            val intent = Intent(holder.itemView.context, Add_Plan::class.java)
            intent.putExtra("idPlanbook", id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((BookDetail) -> Unit)? = null

    private fun notifyEmptyList() {
        Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show()
    }
}
