package com.example.noteapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.data.Note


@Database(entities = [Category::class, ItemOfList::class, Note::class], version = 1)
abstract class DataBase:RoomDatabase() {

    abstract fun categoryDao():CategoryDao
    abstract fun itemDao():ItemDao
    abstract fun notesDao():NoteDao
}