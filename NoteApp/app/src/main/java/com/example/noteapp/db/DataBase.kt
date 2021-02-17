package com.example.noteapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteapp.data.*


@Database(entities = [Category::class, ItemOfList::class, Note::class], version = 1)
@TypeConverters()
abstract class DataBase:RoomDatabase() {

    abstract fun categoryDao():CategoryDao
    abstract fun itemDao():ItemDao
    abstract fun notesDao():NoteDao
}