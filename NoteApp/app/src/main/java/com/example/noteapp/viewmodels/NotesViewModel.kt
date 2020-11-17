package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.data.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(app:Application):AndroidViewModel(app) {
    private val repository = Repository(app)

    val allNotes = repository.getAllNotes()

    private val selectedNote = MutableLiveData<Note?>() // MutableLiveData pozwala na zmiane obiektów

    fun getSelectedNote(): LiveData<Note?> = selectedNote //LiveData nie pozwala na zmiane obiektów

    fun setSelectedNote(note:Note){
        selectedNote.postValue(note)
    }

    fun insert(note: Note){
        CoroutineScope(Dispatchers.IO).launch { repository.insert(note) }
    }

    fun update(note:Note){
        CoroutineScope(Dispatchers.IO).launch { repository.update(note) }
    }

    fun delete(listNotes:List<Note>){
        CoroutineScope(Dispatchers.IO).launch { repository.delete(listNotes) }
    }

    fun clearDatabase(){
        CoroutineScope(Dispatchers.IO).launch { repository.clearDatabase() }
    }
}