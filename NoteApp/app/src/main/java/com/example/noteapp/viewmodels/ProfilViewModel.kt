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

    /*
    fun getNotesFromFirebase(){
        repository.getUserData().value?.forEach {

            Log.d("fireee", "${it.message}")

            val titlee = it.title
            val messagee = it.message
            val datee = it.date
            val isSelectedd = it.isSelected
            val colorr = it.color
            val imagePathh = it.imagePath
            val fontColorr = it.fontColor
            val fontSizee = it.fontSize
            val fav = it.isFavourite
            val hasPasswordd = it.hasPassword
            val passwordd = it.password

            val note = Note(titlee, messagee, datee, false, colorr, imagePathh, fontColorr, fontSizee, fav, hasPasswordd, passwordd).apply {
                rowId =it.rowId
            }

            insertNote(note)
        }
    }

     */

    fun deleteDataFromFirebase(notes:List<Note>){
        repository.clearDataFromFirebase(notes)
    }


}