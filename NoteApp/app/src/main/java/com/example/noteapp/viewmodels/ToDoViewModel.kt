package com.example.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(app:Application):AndroidViewModel(app) {
    var isSearchEdit: Int = 1
    var hasPassword: Boolean = false
    private val repository = Repository(app)

    var search:String?=null

    private val multiSelectMode = MutableLiveData<Boolean?>()
    init {
        multiSelectMode.value = false
    }

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

    fun saveCategoryInCloud(category: Category){
        repository.saveCategoryToCloud(category)
    }

    fun saveTodoListInCloud(categoryId: Int, list:List<ItemOfList>){
        repository.saveTodoListToCloud(categoryId, list)
    }

    var categoryItemBeforeChange: Category? = null

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

    /*
    fun clearDataBaseCategoryItems(){
        CoroutineScope(Dispatchers.IO).launch { repository.deleteCategoryDataBase() }
    }

    fun findCategory(text: String):List<Category>{
        val categoryItems = allCategoryItems.value

        return categoryItems!!.filter { categoryItem ->
            categoryItem.categoryName.toLowerCase().contains(text.toLowerCase()) || categoryItem.categoryName.toUpperCase().contains(text.toUpperCase()) }
    }

     */

    //ItemViewModel

    private lateinit var allItems:LiveData<List<ItemOfList>>

     fun getAllItemsFromCategory(categoryId:Int):LiveData<List<ItemOfList>>{
        return repository.getAllItems(categoryId)
    }

    private val selectedItem = MutableLiveData<ItemOfList?>()

    fun getSelectedItem(): LiveData<ItemOfList?> = selectedItem

    fun setSelectedItem(item: ItemOfList?){
        selectedItem.postValue(item)
    }

    fun insertItem(item: ItemOfList){
        CoroutineScope(Dispatchers.IO).launch { repository.insertItem(item) }
    }

    suspend fun updateItem(item: ItemOfList){
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
}