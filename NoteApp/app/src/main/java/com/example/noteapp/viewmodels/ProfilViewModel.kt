package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.noteapp.data.Note

class ProfilViewModel(app: Application):AndroidViewModel(app) {

    private val repository = Repository(app)

    //Firebase ViewMode

    val userData = repository.getUserData()

    fun addNotesToCloud(notes:List<Note>){
        repository.saveNotesToCloud(notes)
    }

    val notesFromFirebase = repository.getUserData()

    fun deleteDataFromFirebase(notes:List<Note>){
        repository.clearDataFromFirebase(notes)
    }
}