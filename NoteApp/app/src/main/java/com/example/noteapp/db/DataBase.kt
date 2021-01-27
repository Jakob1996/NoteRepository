package com.example.noteapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.data.Note
import com.example.noteapp.data.PathTypeConverter


@Database(entities = [Category::class, ItemOfList::class, Note::class], version = 1)
@TypeConverters(PathTypeConverter::class)
abstract class DataBase:RoomDatabase() {

    abstract fun categoryDao():CategoryDao
    abstract fun itemDao():ItemDao
    abstract fun notesDao():NoteDao
}