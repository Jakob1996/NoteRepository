package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.noteapp.data.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(app:Application):AndroidViewModel(app) {
    private val repository = Repository(app)

    val allNotes = repository.getAllNotes()

    fun insert(note: Note){
        CoroutineScope(Dispatchers.IO).launch { repository.insert(note) }
    }

    fun update(note:Note){
        CoroutineScope(Dispatchers.IO).launch { repository.update(note) }
    }

    fun delete(listNotes:List<Note>){
        CoroutineScope(Dispatchers.IO).launch { repository.delete(listNotes) }
    }
}