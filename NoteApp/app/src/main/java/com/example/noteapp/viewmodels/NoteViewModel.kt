package com.example.noteapp.viewmodels

import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.data.Note
import com.example.noteapp.data.relations.CategoryWithItems
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class NoteViewModel(app:Application):AndroidViewModel(app) {

    var stateFragmentMain:Bundle? = null
    var searchStateFragment:Bundle? = null

    private val repository = Repository(app)

    var search:String?=null

    //NoteViewModel

    val allNotes = repository.getAllNotes()

    fun getAllN(): LiveData<List<Note>> = repository.getAllNotes()

    private val selectedNote = MutableLiveData<Note?>()

    fun getSelectedNote(): LiveData<Note?> = selectedNote

    fun setSelectedNote(note: Note?){
        selectedNote.postValue(note)
    }

    var noteBeforeChange:Note? = null
    var titleBefore:String? =null
    var messageBefore:String? =null

    private var searchMode = MutableLiveData<Boolean?>()

    fun getSearchMode(): LiveData<Boolean?> = searchMode

    fun setSearchMode(boolean: Boolean?){
        searchMode.postValue(boolean)
    }

    var isSearchEdit = 1

    private var fabNoteButtonMode = MutableLiveData<Boolean?>()
    fun getNoteFabButtonMode():LiveData<Boolean?> = fabNoteButtonMode

    fun setNoteFabButtonMode(boolean: Boolean?){
        fabNoteButtonMode.postValue(boolean)
    }

    var searchInNote:String = ""
    var noteState:Parcelable? = null
    var categoryToDoState:Parcelable? = null
    var selectedNoteColor = "#333333"
    var selectedFontNote:Int = 1
    var selectedFontSize:Int = 3
    var noteTitle = ""
    var noteMessage = ""
    var noteDate: Long= 1
    var pathImage = arrayListOf<String?>()
    var idNote = 1
    var hasPassword = false
    var password:Int = 0
    var isFavourite = false

    private val sortDescNote = MutableLiveData<Boolean?>()

    fun getSortDescNote():LiveData<Boolean?> = sortDescNote

    fun setSortDescNote(boolean: Boolean){
        sortDescNote.postValue(boolean)
    }

    private val multiSelectMode = MutableLiveData<Boolean?>()
    init {
        multiSelectMode.value = false
    }

    fun getMultiSelectMode():LiveData<Boolean?> = multiSelectMode
    fun setMutliSelectMode(boolean: Boolean?){
        multiSelectMode.postValue(boolean)
    }

    private var fabCategoryButtonMode = MutableLiveData<Boolean?>()
    fun getCategoryFabButtonMode():LiveData<Boolean?> = fabCategoryButtonMode

    fun setCategoryFabButtonMode(boolean: Boolean?){
        fabCategoryButtonMode.postValue(boolean)
    }

    private val notifyDataNote = MutableLiveData<Boolean?>()
    init {
        notifyDataNote.value = false
    }

    fun getNotifyDataNote():LiveData<Boolean?> = notifyDataNote

    fun setNotifyDataNote(boolean: Boolean?){
        notifyDataNote.postValue(boolean)
    }

    private val sortDescCategory = MutableLiveData<Boolean?>()

    fun getSortDescCategory():LiveData<Boolean?> = sortDescCategory

    fun setSortDescCategory(boolean: Boolean){
        sortDescCategory.postValue(boolean)
    }

    private val notifyDataCategory = MutableLiveData<Boolean?>()
    init {
        notifyDataCategory.value = false
    }

    fun getNotifyDataCategory():LiveData<Boolean?> = notifyDataCategory

    fun setNotifyDataCategory(boolean: Boolean?){
        notifyDataCategory.postValue(boolean)
    }

    var newNote = false

    val selectedNotes = ArrayList<Note>()

    fun insertNote(note: Note){
        CoroutineScope(Dispatchers.IO).launch { repository.insertNote(note) }
    }

    fun updateNote(note:Note){
        CoroutineScope(Dispatchers.IO).launch { repository.updateNote(note) }
    }

    fun deleteNotes(listNotes:List<Note>){
         repository.deleteNotes(listNotes)
    }

    /*
    fun deleteOneNote(note: Note?){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteOneNote(note) }
    }

    fun clearDatabaseNotes(){
        CoroutineScope(Dispatchers.IO).launch { repository.clearDatabaseNotes() }
    }


     */
    //State Model

    var p:Boolean = true
}