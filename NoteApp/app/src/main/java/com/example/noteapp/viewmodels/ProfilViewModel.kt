package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.Note

class ProfilViewModel(app: Application):AndroidViewModel(app) {

    private val repository = Repository(app)

    //Firebase ViewMode

    fun addNotesToCloud(notes:List<Note>){
        repository.saveNotesToCloud(notes)
    }
    fun getNotesFromFirebase():LiveData<List<Note>> = repository.getUserNotesData()
     //var getNotesFromFirebase = repository.getUserNotesData()

    fun getCategoriesFromFirebase():LiveData<List<Category>> = repository.getUserCategoryFromFirebase()

    fun deleteDataFromFirebase(notes:List<Note>){
        repository.clearDataFromFirebase(notes)
    }

    fun updateNoteInCloud(note:Note){
        repository.updateNoteInCloud(note)
    }
}