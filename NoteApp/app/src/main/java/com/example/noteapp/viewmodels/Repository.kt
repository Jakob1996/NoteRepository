package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.data.Note
import com.example.noteapp.db.DataBaseBuilder

class Repository (app:Application) {
    private val builder = DataBaseBuilder.getInstance(app.applicationContext)
    private val notesDao = builder.notesDao()
    private val categoryDao = builder.categoryDao()
    private val itemDao = builder.itemDao()

    //Note repository
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

    //Category repository

    suspend fun insertCategory(category: Category){
        categoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: Category){
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category:List<Category>){
        categoryDao.deleteCategory(category)
    }

    suspend fun deleteCategoryDataBase(){
        categoryDao.clearDataBaseCategory()
    }

    fun getAllCategory():LiveData<List<Category>>{
        return categoryDao.getAllCategory().asLiveData()
    }

    //Items repository

    suspend fun insertItem(item: ItemOfList){
        itemDao.insertItem(item)
    }

    suspend fun deleteItem(item: ItemOfList){

        itemDao.deleteItem(item)
    }

    suspend fun deleteItems(items:List<ItemOfList>){
        itemDao.deleteItems(items)
    }

    suspend fun updateItem(item: ItemOfList){
        itemDao.updateItem(item)
    }

     fun getAllItems(categoryId: Int):LiveData<List<ItemOfList>>{
        return itemDao.getAllItems(categoryId).asLiveData()
    }

     fun deleteAllITems(categoryId: Int){
        itemDao.deleteAllItems(categoryId)
    }

    fun getAllI():LiveData<List<ItemOfList>>{
        return itemDao.getAllI().asLiveData()
    }


}