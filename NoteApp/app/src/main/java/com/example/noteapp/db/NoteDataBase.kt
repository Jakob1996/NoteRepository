package com.example.noteapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapp.data.Note

//Baza danych

@Database(entities = [Note::class], version = 1)
abstract class NoteDataBase:RoomDatabase() {

    abstract fun noteDao():NoteDao
}