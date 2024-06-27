package com.example.smartlibrary1.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.smartlibrary1.data.BookDetail
import com.example.smartlibrary1.data.Note
import com.example.smartlibrary1.data.Plan


class BookDatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION) {
    // Database creation sql statement
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "nhom10_20213829.db"


        const val TABLE_BOOKS = "books"
        const val TABLE_NOTES = "notes"
        const val TABLE_PLANS = "plans"




        // Books table columns
        const val COLUMN_BOOK_ID = "bookid"
        const val COLUMN_BOOK_AUTHOR = "author"
        const val COLUMN_BOOK_GENRE = "genre"
        const val COLUMN_BOOK_DESCRIPTION = "description"
        const val COLUMN_BOOK_TITLE = "title"
        const val COLUMN_BOOK_IMAGE = "image"
        const val COLUMN_BOOK_URL = "url"


        // Note table columns
        const val COLUMN_NOTE_ID = "idNote"
        const val COLUMN_NOTE_BOOK_ID = "idBook"
        const val COLUMN_NOTE_TIME = "time_note"
        const val COLUMN_NOTE_PAGE = "page"
        const val COLUMN_NOTE_CONTENT = "content"
        const val COLUMN_NOTE_DATE = "date_note"

        // Plan table columns
        const val COLUMN_PLAN_ID = "idPlan"
        const val COLUMN_PLAN_BOOK_ID = "idPlanbook"
        const val COLUMN_PLAN_TITLE = "titlePlan"
        const val COLUMN_PLAN_MESSAGE = "message"
        const val COLUMN_PLAN_DAY = "day"
        const val COLUMN_PLAN_MONTH = "month"
        const val COLUMN_PLAN_YEAR = "year"
        const val COLUMN_PLAN_HOUR = "hour"
        const val COLUMN_PLAN_MINUTE = "minute"


    }

    override fun onCreate(db: SQLiteDatabase) {


        val createBooksTableQuery = "CREATE TABLE $TABLE_BOOKS (" +
                "$COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_BOOK_AUTHOR TEXT," +
                "$COLUMN_BOOK_GENRE TEXT," +
                "$COLUMN_BOOK_DESCRIPTION TEXT," +
                "$COLUMN_BOOK_TITLE TEXT," +
                "$COLUMN_BOOK_IMAGE TEXT," +
                "$COLUMN_BOOK_URL TEXT)"
        db.execSQL(createBooksTableQuery)


        val createNotesTableQuery = "CREATE TABLE $TABLE_NOTES (" +
                "$COLUMN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOTE_BOOK_ID INTEGER," +
                "$COLUMN_NOTE_CONTENT TEXT,"  +
                "$COLUMN_NOTE_DATE TEXT," +
                "$COLUMN_NOTE_PAGE INTEGER," +
                "$COLUMN_NOTE_TIME TEXT," +
                "FOREIGN KEY ($COLUMN_NOTE_BOOK_ID) REFERENCES $TABLE_BOOKS ($COLUMN_BOOK_ID))"
        db.execSQL(createNotesTableQuery)

        val createPlansTableQuery = "CREATE TABLE $TABLE_PLANS (" +
                "$COLUMN_PLAN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_PLAN_BOOK_ID INTEGER," +
                "$COLUMN_PLAN_TITLE TEXT,"  +
                "$COLUMN_PLAN_MESSAGE TEXT," +
                "$COLUMN_PLAN_DAY INTEGER," +
                "$COLUMN_PLAN_MONTH INTEGER," +
                "$COLUMN_PLAN_YEAR INTEGER," +
                "$COLUMN_PLAN_HOUR INTEGER,"+
                "$COLUMN_PLAN_MINUTE INTEGER," +
                "FOREIGN KEY ($COLUMN_NOTE_BOOK_ID) REFERENCES $TABLE_BOOKS ($COLUMN_BOOK_ID))"
        db.execSQL(createPlansTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Upgrade logic

    }


    fun insertBook(author: String, genre: String, description: String, title: String, image: String, url: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BOOK_AUTHOR, author)
            put(COLUMN_BOOK_GENRE, genre)
            put(COLUMN_BOOK_DESCRIPTION,description)
            put(COLUMN_BOOK_TITLE, title)
            put(COLUMN_BOOK_IMAGE, image)
            put(COLUMN_BOOK_URL, url)
        }
        val id = db.insert(TABLE_BOOKS, null, values)
        db.close()
        return id
    }


    fun insertNote(note: Note): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTE_BOOK_ID, note.idbook)
            put(COLUMN_NOTE_CONTENT, note.content)
            put(COLUMN_NOTE_DATE, note.date_note)
            put(COLUMN_NOTE_TIME, note.time_note)
            put(COLUMN_NOTE_PAGE, note.page)
        }
        val id = db.insert(TABLE_NOTES, null, values)
        db.close()
        return id
    }

    fun insertPlan(plan: Plan): Long{
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PLAN_BOOK_ID, plan.idPlanBook)
            put(COLUMN_PLAN_TITLE, plan.title)
            put(COLUMN_PLAN_MESSAGE, plan.message)
            put(COLUMN_PLAN_DAY, plan.day)
            put(COLUMN_PLAN_MONTH, plan.month)
            put(COLUMN_PLAN_YEAR, plan.year)
            put(COLUMN_PLAN_HOUR, plan.hour)
            put(COLUMN_PLAN_MINUTE, plan.minute)
        }
        val id = db.insert(TABLE_PLANS, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getAllBooks(): MutableList<BookDetail> {
        var i = 0
        val books = mutableListOf<BookDetail>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM books", null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex("bookid"))
                val author = it.getString(it.getColumnIndex("author"))
                val genre = it.getString(it.getColumnIndex("genre"))
                val description = it.getString(it.getColumnIndex("description"))
                val title = it.getString(it.getColumnIndex("title"))
                val image = it.getString(it.getColumnIndex("image"))
                val book = BookDetail( title, image, description, author,genre)
                books.add(book)
                println(i++)
            }
        }

        return books
    }


    fun deleteBookById(bookId: Int): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_BOOKS, "$COLUMN_BOOK_ID = ?", arrayOf(bookId.toString())) > 0
    }


    @SuppressLint("Range")
    fun getIdBookByTitle(title: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_BOOK_ID FROM $TABLE_BOOKS WHERE $COLUMN_BOOK_TITLE = ?", arrayOf(title))
        var bookId: Int? = null
        cursor.use {
            if (it.moveToFirst()) {
                bookId = it.getInt(it.getColumnIndex(COLUMN_BOOK_ID))
            }
        }
        db.close()
        return bookId
    }

    @SuppressLint("Range")
    fun getNotesByBookId(bookId: Int): Note? {
        var note: Note? = null
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NOTES WHERE $COLUMN_NOTE_BOOK_ID = ?", arrayOf(bookId.toString()))
        cursor.use {
            if (it.moveToFirst()) {

                val idBook = it.getInt(it.getColumnIndex(COLUMN_NOTE_BOOK_ID))
                val content = it.getString(it.getColumnIndex(COLUMN_NOTE_CONTENT))
                val date = it.getString(it.getColumnIndex(COLUMN_NOTE_DATE))
                val page = it.getInt(it.getColumnIndex(COLUMN_NOTE_PAGE))
                val time = it.getString(it.getColumnIndex(COLUMN_NOTE_TIME))

                note = Note(
                    idbook = idBook,
                    time_note = time,
                    date_note = date,
                    page = page,
                    content = content
                )
            }
        }
        db.close()
        return note
    }

    @SuppressLint("Range")
    fun getPlanByBookID(bookId: Int): Plan?{
        var plan: Plan? = null
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PLANS WHERE $COLUMN_PLAN_BOOK_ID = ?", arrayOf(bookId.toString()))
        cursor.use {
            if (it.moveToFirst()) {

                val idBook = it.getInt(it.getColumnIndex(COLUMN_PLAN_BOOK_ID))
                val title = it.getString(it.getColumnIndex(COLUMN_PLAN_TITLE))
                val message = it.getString(it.getColumnIndex(COLUMN_PLAN_MESSAGE))
                val day = it.getInt(it.getColumnIndex(COLUMN_PLAN_DAY))
                val month = it.getInt(it.getColumnIndex(COLUMN_PLAN_MONTH))
                val year = it.getInt(it.getColumnIndex(COLUMN_PLAN_YEAR))
                val hour = it.getInt(it.getColumnIndex(COLUMN_PLAN_HOUR))
                val minute = it.getInt(it.getColumnIndex(COLUMN_PLAN_MINUTE))

                plan = Plan(
                    idPlanBook = idBook,
                    title = title,
                    message = message,
                    day = day,
                    month = month,
                    year = year,
                    hour = hour,
                    minute = minute
                )
            }
        }
        db.close()
        return plan

    }

    fun deleteNoteByBookId(bookId: Int): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_NOTES, "$COLUMN_NOTE_BOOK_ID = ?", arrayOf(bookId.toString())) > 0
    }
    fun deletePlanByBookId(bookId: Int): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_PLANS, "$COLUMN_PLAN_BOOK_ID = ?", arrayOf(bookId.toString())) > 0
    }



}