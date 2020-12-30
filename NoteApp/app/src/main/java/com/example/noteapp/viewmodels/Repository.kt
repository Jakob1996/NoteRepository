package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemsOfList
import com.example.noteapp.data.Note
import com.example.noteapp.db.DataBaseBuilder

class Repository (app:Application) {
    private val builder = DataBaseBuilder.getInstance(app.applicationContext)
    private val notesDao = builder.notesDao()
    private val categoryDao = builder.categoryDao()
    private val itemDao = builder.itemDao()


    //Note
    suspend fun insertNote(note: Note){
        notesDao.insertNote(note)
    }

    suspend fun updateNote(note:Note){
        notesDao.updateNote(note)
    }

    suspend fun deleteNotes(list: List<Note>){
        notesDao.deleteNotes(list)
    }

    suspend fun deleteOneNote(note: Note?){
        notesDao.deleteOneNote(note)
    }

    suspend fun clearDatabaseNotes(){
        notesDao.clearDatabaseNotes()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return notesDao.getAllNotes().asLiveData()
    }

    //Category

    suspend fun insertCategory(category: Category){
        categoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: Category){
        categoryDao.updateCategoty(category)
    }

    suspend fun deleteCategory(category:List<Category>){
        categoryDao.deleteCategory(category)
    }

    suspend fun deleteCategoryDataBase(){
        categoryDao.clearDataBaseCategory()
    }

    fun getAllCategoryItems():LiveData<List<Category>>{
        return categoryDao.getAllCategoryItems().asLiveData()
    }

    //Items

    suspend fun insertItem(item:ItemsOfList){
        itemDao.insertItem(item)
    }

    suspend fun deleteItem(item: ItemsOfList){

        itemDao.deleteItem(item)
    }

    suspend fun deleteItems(items:List<ItemsOfList>){
        itemDao.deleteItems(items)
    }

    suspend fun updateItem(item: ItemsOfList){
        itemDao.updateItem(item)
    }

    suspend fun getAllItems(categoryName: String):LiveData<List<ItemsOfList>>{
        return itemDao.getAllItems(categoryName).asLiveData()
    }

    suspend fun deleteAllITems(categoryName:String){
        itemDao.deleteAllItems(categoryName)
    }
}