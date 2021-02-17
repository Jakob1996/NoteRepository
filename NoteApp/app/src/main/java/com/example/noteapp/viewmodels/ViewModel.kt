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

class ViewModel(app:Application):AndroidViewModel(app) {

    var stateFragmentMain:Bundle? = null
    var searchStateFragment:Bundle? = null

    private val repository = Repository(app)

    var search:String?=null

    //Room ViewModel

    //NoteViewModel

    val allNotes = repository.getAllNotes()

    private val selectedNote = MutableLiveData<Note?>()

    fun getSelectedNote(): LiveData<Note?> = selectedNote

    fun setSelectedNote(note: Note?){
        selectedNote.postValue(note)
    }

    /*
    private val noteBeforeChan = MutableLiveData<Note?>()

    fun getSelectedNoteBeforeChange(): LiveData<Note?> {
        return noteBeforeChan
    }

    fun setSelectedNoteBeforeChange(note: Note?){
        noteBeforeChan.postValue(note)
    }

     */

    var noteBeforeChange:Note? = null

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

    fun deleteOneNote(note: Note?){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteOneNote(note) }
    }

    fun clearDatabaseNotes(){
        CoroutineScope(Dispatchers.IO).launch { repository.clearDatabaseNotes() }
    }

    /*
    fun findInNotes(text:String): List<Note> {
        val list = allNotes.value
         return list!!.filter { note ->
            note.title.toLowerCase().contains(text.toLowerCase()) || note.message.toUpperCase().contains(text.toUpperCase())
        }
    }

     */

    //CategoryViewModel

    var categoryName:String = ""
    var selectedCategotyItemColor = "#333333"
    var categoryIsSelected:Boolean = false
    var categoryDate:Long=0
    var isFavouriteCategory:Boolean = false
    var hasPasswordCategory:Boolean = false
    var passwordCategory:Int = 0
    var categoryId:Int =0

    val allCategoryItems = repository.getAllCategory()

    private val selectedCategoryItem = MutableLiveData<Category?>()

    fun getSelectedCategotyItem(): LiveData<Category?> = selectedCategoryItem

    fun setSelectedCategotyItem(category: Category?){
        selectedCategoryItem.postValue(category)
    }

    /*
    private var selectedCategoryItemBefore = MutableLiveData<Category?>()

    fun getSelectedCategoryItemBefore():LiveData<Category?> = selectedCategoryItemBefore

    fun setSelectedCategoryItemBefore(category:Category?){
        selectedCategoryItemBefore.postValue(category)
    }
     */

    var categoryItemBeforeChange:Category? = null

    var sortDescCategoryItem = false


    val selectedCategoryItems = ArrayList<Category>()

    fun insertCategotyItem (category: Category){
        CoroutineScope(Dispatchers.IO).launch { repository.insertCategory(category) }
    }

    fun updateCategoryItem(category: Category){
        CoroutineScope(Dispatchers.IO).launch { repository.updateCategory(category) }
    }

    fun deleteCategotyItems(category:List<Category>){
      repository.deleteCategory(category)
    }

    fun clearDataBaseCategoryItems(){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteCategoryDataBase() }
    }

    fun findCategory(text: String):List<Category>{
        val categoryItems = allCategoryItems.value

        return categoryItems!!.filter { categoryItem ->
            categoryItem.categoryName.toLowerCase().contains(text.toLowerCase()) || categoryItem.categoryName.toUpperCase().contains(text.toUpperCase()) }
    }

    //ItemViewModel

    private lateinit var allItems:LiveData<List<ItemOfList>>

     fun getAllItemsFromCategory(categoryId:Int):LiveData<List<ItemOfList>>{
        return repository.getAllItems(categoryId)
}

    private val selectedItem = MutableLiveData<ItemOfList?>()

    fun getSelectedItem(): LiveData<ItemOfList?> = selectedItem

    fun setSelectedItem(item:ItemOfList?){
        selectedItem.postValue(item)
    }

    fun insertItem(item:ItemOfList){
        CoroutineScope(Dispatchers.IO).launch { repository.insertItem(item) }
    }

    fun updateItem(item: ItemOfList){
        CoroutineScope(Dispatchers.IO).launch { repository.updateItem(item) }
    }

    fun deleteItem(item: ItemOfList){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteItem(item) }
    }

    fun deleteItems(categoryId: Int){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteAllITems(categoryId) }
    }

    //State Model

    var p:Boolean = true

    /*
    fun getCategoryWithItems(categoryId:Int):List<CategoryWithItems>{
        return CoroutineScope(Dispatchers.IO).launch { repository.getCategoryWithItems(categoryId) }
    }

     */
}