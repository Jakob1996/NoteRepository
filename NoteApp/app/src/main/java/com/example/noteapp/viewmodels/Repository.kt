package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.Note
import com.example.noteapp.db.NoteDataBaseBuilder

class Repository (app:Application) {
    private val notesDao = NoteDataBaseBuilder.getInstance(app.applicationContext).noteDao()

    suspend fun insert(note: Note){
        notesDao.insert(note)
    }

    suspend fun update(note:Note){
        notesDao.update(note)
    }

    suspend fun delete(list: List<Note>){
        notesDao.deleteNotes(list)
    }

    suspend fun clearDatabase(){
        notesDao.clearDatabase()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return notesDao.getAllNotes().asLiveData()
    }
}