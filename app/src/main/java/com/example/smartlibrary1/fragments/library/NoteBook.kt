package com.example.smartlibrary1.fragments.library

import android.os.Bundle
import android.widget.ImageButton

import androidx.appcompat.app.AppCompatActivity

import com.example.smartlibrary1.R
import android.widget.Toast

import com.example.smartlibrary1.data.Note
import com.example.smartlibrary1.databinding.ActivityNoteBookBinding
import com.example.smartlibrary1.helper.BookDatabaseHelper

class NoteBook : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBookBinding
    private lateinit var db: BookDatabaseHelper
    private var idbook: Int? = null  // Khai báo idbook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBookBinding.inflate(layoutInflater)

        setContentView(binding.root)
//        val backBtn = findViewById<ImageButton>(R.id.backBtn)
//        backBtn.setOnClickListener {
//            onBackPressed()
//        }

        db = BookDatabaseHelper(this)

        idbook = intent.getIntExtra("idbook", -1)

        // Kiểm tra nếu idbook không hợp lệ
        if (idbook == -1) {
            Toast.makeText(this, "Error retrieving book ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val existingNote = db.getNotesByBookId(idbook!!)
        if (existingNote != null) {
            binding.timeEt.setText(existingNote.time_note)
            binding.numpageEt.setText(existingNote.page.toString())
            binding.dateEt.setText(existingNote.date_note)
            binding.noteEt.setText(existingNote.content)
        }

        binding.submitBtn.setOnClickListener {
            db.deleteNoteByBookId(idbook!!)
            val time = binding.timeEt.text.toString()
            val page = binding.numpageEt.text.toString().toInt()
            val date = binding.dateEt.text.toString()
            val content = binding.noteEt.text.toString()
            val note = Note(idbook!!, time,date,page,content)
            db.insertNote(note)
            finish()
            Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show()
        }



        binding.deleBtn.setOnClickListener {
            val success = db.deleteNoteByBookId(idbook!!)
            if (success) {
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()
                binding.timeEt.text.clear()
                binding.numpageEt.text.clear()
                binding.dateEt.text.clear()
                binding.noteEt.text.clear()
            } else {
                Toast.makeText(this, "Error Deleting Note", Toast.LENGTH_SHORT).show()
            }

        }

    }


}