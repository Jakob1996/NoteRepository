package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.data.Note

class ProfilViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = Repository(app)

    fun addNotesToCloud(notes: List<Note>) {
        repository.saveNotesToCloud(notes)
    }

    fun getNotesFromFirebase(): LiveData<List<Note>> = repository.getUserNotesData()

    fun updateNoteInCloud(note: Note) {
        repository.updateNoteInCloud(note)
    }

//    fun saveCategoriesToCloud(categories: List<Category>) {
//        repository.saveCategoriesToCloud(categories)
//    }
//    fun saveTodoItemsToCloud(itemsList: List<ItemTodo>) {
//        repository.saveTodoItemsToCloud()
//    }
//
//    fun saveCategoriesToRoom(categories: List<Category>) {
//        repository.saveCategoriesToRoom(categories)
//    }
//
//    fun saveTodoItemsToRoom(itemsList: List<ItemTodo>) {
//        repository.saveTodoItemsToRoom()
//    }

    //Category
    fun getCategoriesFromFirebase(): LiveData<List<Category>> =
        repository.getUserCategoryFromFirebase()

    fun getAllCategoryFromRoom(): LiveData<List<Category>> =
        repository.getAllCategory()

    fun saveCategoryInCloud(category: Category) =
        repository.saveCategoryToCloud(category)

    //Todo items
    fun saveTodoListInCloud(categoryId: Int, list: List<ItemOfList>) =
        repository.saveTodoListToCloud(categoryId, list)

//    fun getAllTodoItemsFromRoom(): LiveData<List<ItemTodo>> =
//        repository.getAllTodoItems()

    fun deleteDataFromFirebase(notes: List<Note>) =
        repository.clearDataFromFirebase(notes)

    fun deleteCategoriesFromFirebase(categories: List<Category>) =
        repository.deleteCategoryItemsFromFirebase(categories)
}