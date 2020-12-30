package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemsOfList
import com.example.noteapp.data.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(app:Application):AndroidViewModel(app) {
    private val repository = Repository(app)

    //NoteViewModel
    val allNotes = repository.getAllNotes()

    private val selectedNote = MutableLiveData<Note?>() // MutableLiveData pozwala na zmiane obiektów

    fun getSelectedNote(): LiveData<Note?> = selectedNote //LiveData nie pozwala na zmiane obiektów

    fun setSelectedNote(note: Note?){
        selectedNote.postValue(note)
    }

    var selectedNoteColor = "#333333"
    var sortDescNote = true
    private val multiSelectNote = MutableLiveData<Boolean?>()
    init {
        multiSelectNote.value = false
    }

    fun getMultiSelectNote():LiveData<Boolean?> = multiSelectNote
    fun setMutliSelectNote(boolean: Boolean?){
        multiSelectNote.postValue(boolean)
    }



    /*
    private var multiSelectModeNote = false
    var getMultiSelectModeNote = multiSelectModeNote
    fun setMutliSelectModeNote(boolean: Boolean){
        multiSelectModeNote = boolean
        mulVal.value = boolean
    }
     */

    val selectedNotes = ArrayList<Note>()

    fun insertNote(note: Note){
        CoroutineScope(Dispatchers.IO).launch { repository.insertNote(note) }
    }

    fun updateNote(note:Note){
        CoroutineScope(Dispatchers.IO).launch { repository.updateNote(note) }
    }

    fun deleteNotes(listNotes:List<Note>){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteNotes(listNotes) }
    }

    fun deleteOneNote(note: Note?){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteOneNote(note) }
    }

    fun clearDatabaseNotes(){
        CoroutineScope(Dispatchers.IO).launch { repository.clearDatabaseNotes() }
    }

    fun findInNotes(text:String): List<Note> {
        val list = allNotes.value
         return list!!.filter { note ->
            note.title.toLowerCase().contains(text.toLowerCase()) || note.message.toUpperCase().contains(text.toUpperCase())
        }
    }

    //CategoryViewModel

    val allCategoryItems = repository.getAllCategoryItems()

    private val selectedCategoryItem = MutableLiveData<Category?>()

    fun getSelectedCategotyItem(): LiveData<Category?> = selectedCategoryItem

    fun setSelectedCategotyItem(category: Category?){
        selectedCategoryItem.postValue(category)
    }

    var selectedCategotyItemColor = "#333333"
    var sortDescCategoryItem = true


    private val multiSelectCategotyMode = MutableLiveData<Boolean?>()
    init {
        multiSelectCategotyMode.value = false
    }

    fun getMultiSelectCategoryMode():LiveData<Boolean?> = multiSelectCategotyMode
    fun setMutliSelectCategoryMode(boolean: Boolean?){
        multiSelectCategotyMode.postValue(boolean)
    }


    val selectedCategoryItems = ArrayList<Category>()

    fun insertCategotyItem (category: Category){
        CoroutineScope(Dispatchers.IO).launch { repository.insertCategory(category) }
    }

    fun updateCategoryItem(category: Category){
        CoroutineScope(Dispatchers.IO).launch { repository.updateCategory(category) }
    }

    fun deleteCategotyItems(category:List<Category>){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteCategory(category) }
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

    private lateinit var allItems:LiveData<List<ItemsOfList>>

    suspend fun getAllItemsFromCategory(categoryName:String):LiveData<List<ItemsOfList>>{
        return repository.getAllItems(categoryName)
}

    private val selectedItem = MutableLiveData<ItemsOfList?>()

    fun getSelectedItem(): LiveData<ItemsOfList?> = selectedItem

    fun setSelectedItem(item:ItemsOfList?){
        selectedItem.postValue(item)
    }

    fun insertItem(item:ItemsOfList){
        CoroutineScope(Dispatchers.IO).launch { repository.insertItem(item) }
    }

    fun updateItem(item: ItemsOfList){
        CoroutineScope(Dispatchers.IO).launch { repository.updateItem(item) }
    }

    fun deleteItem(item: ItemsOfList){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteItem(item) }
    }

    fun deleteItems(categoryName:String){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteAllITems(categoryName) }
    }
}