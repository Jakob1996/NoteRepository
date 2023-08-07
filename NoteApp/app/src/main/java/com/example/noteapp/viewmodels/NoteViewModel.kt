package com.example.noteapp.viewmodels

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentGeneralNoteBinding
import com.example.noteapp.databinding.FragmentMainBinding
import com.example.noteapp.databinding.FragmentNotesListBinding
import kotlinx.coroutines.*

class NoteViewModel(app: Application) : AndroidViewModel(app) {

    val sharePreferences = SharePreferences(app.applicationContext)

    var view: FragmentMainBinding? = null

    var view2: FragmentGeneralNoteBinding? = null

    var view3: FragmentNotesListBinding? = null

    private val repository = Repository(app)

    var search: String? = null

    //NoteViewModel

    val getAllNotes = repository.getAllNotes()

    private val selectedNote = MutableLiveData<Note?>()

    fun getSelectedNote(): LiveData<Note?> = selectedNote

    fun setSelectedNote(note: Note?) {
        selectedNote.postValue(note)
    }

    var noteBeforeChange: Note? = null

    var titleBefore: String? = null

    var messageBefore: String? = null

    private var searchMode = MutableLiveData<Boolean?>()

    fun getSearchMode(): LiveData<Boolean?> = searchMode

    fun setSearchMode(boolean: Boolean?) {
        searchMode.postValue(boolean)
    }

    var isSearchEdit = 1

    var fabNoteButtonMode = MutableLiveData<Boolean?>()
    fun setNoteFabButtonMode(boolean: Boolean?) {
        fabNoteButtonMode.postValue(boolean)
    }

    var searchInNote: String = ""
    var noteState: Parcelable? = null
    var categoryToDoState: Parcelable? = null
    var selectedNoteColor = "#333333"
    var selectedFontNote: Int = 1
    var selectedFontSize: Int = 3
    var noteTitle = ""
    var noteMessage = ""
    var noteDate: Long = 1
    var pathImage = mutableListOf<String>()
    var idNote = 1
    var hasPassword = false
    var password: Int = 0
    var isFavourite = false

    val isSortDescendingNoteStateAction = MutableLiveData<Boolean?>()

    private val multiSelectMode = MutableLiveData<Boolean?>()

    init {
        multiSelectMode.value = false
    }

    fun getMultiSelectMode(): LiveData<Boolean?> = multiSelectMode

    fun setMultiSelectMode(boolean: Boolean?) {
        multiSelectMode.postValue(boolean)
    }

    private var fabCategoryButtonMode = MutableLiveData<Boolean?>()
    fun getCategoryFabButtonMode(): LiveData<Boolean?> = fabCategoryButtonMode

    fun setCategoryFabButtonMode(boolean: Boolean?) {
        fabCategoryButtonMode.postValue(boolean)
    }

    private val notifyDataNote = MutableLiveData<Boolean?>()

    init {
        notifyDataNote.value = false
    }

    fun getNotifyDataNote(): LiveData<Boolean?> = notifyDataNote

    fun setNotifyDataNote(boolean: Boolean?) {
        notifyDataNote.postValue(boolean)
    }

    val sortDescendingNote = MutableLiveData<Boolean?>()

    private val notifyDataCategory = MutableLiveData<Boolean?>()

    init {
        notifyDataCategory.value = false
    }

    fun getNotifyDataCategory(): LiveData<Boolean?> = notifyDataCategory

    fun setNotifyDataCategory(boolean: Boolean?) {
        notifyDataCategory.postValue(boolean)
    }

    var newNote = false

    val selectedNotes = ArrayList<Note>()

    fun insertNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch { repository.insertNote(note) }
    }

    fun updateNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch { repository.updateNote(note) }
    }

    fun deleteNotes(listNotes: List<Note>) {
        repository.deleteNotes(listNotes)
    }

    //State Model

    fun setDefaultNoteState() {
        setSearchMode(false)
        setSelectedNote(null)
        noteBeforeChange = null
        noteTitle = ""
        noteMessage = ""
        noteDate = 1
        pathImage = arrayListOf()
        selectedFontSize = 3
        selectedFontNote = 1
        selectedNoteColor = "#333333"
        idNote = -1
        isFavourite = false
        hasPassword = false
        password = 0
        isSearchEdit = 1
    }

    //Check password fragment
    private var isFromMainFragmentNavigation = true

    fun setIsFromMainFragmentNavigation(isFromMain: Boolean) {
        isFromMainFragmentNavigation = isFromMain
    }

    fun getIsFromMainFragmentNavigation() = isFromMainFragmentNavigation
}