package com.example.noteapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.data.Note
import com.example.noteapp.db.DataBaseBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Repository(app: Application) {
    private val REPO_DEBUG = "REPO_DEBUG"

    //Firebase Repository
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid

    //Note firebase
    fun getUserNotesData(): LiveData<List<Note>> {
        val cloudResult = MutableLiveData<List<Note>>()
        val uid = auth.currentUser?.uid
        if (auth.currentUser != null) {
            cloud.collection("users")
                .document(uid!!)
                .collection("notes")
                //addSnapshotListener { value, _ ->
                .get()
                .addOnSuccessListener {
                    val res = it?.toObjects(Note::class.java)
                    cloudResult.value = res!!
                }.addOnFailureListener {
                    Log.d(REPO_DEBUG, it.message.toString())
                }
        }

        return cloudResult
    }

    fun getUserCategoryFromFirebase(): LiveData<List<Category>> {
        val cloudResult = MutableLiveData<List<Category>>()
        val uid = auth.currentUser?.uid
        if (auth.currentUser != null) {
            cloud.collection("users")
                .document(uid!!)
                .collection("todoList")
                //addSnapshotListener { value, _ ->
                .get()
                .addOnSuccessListener {
                    val res = it?.toObjects(Category::class.java)
                    cloudResult.value = res!!
                }.addOnFailureListener {
                    Log.d(REPO_DEBUG, it.message.toString())
                }
        }
        return cloudResult
    }

    fun saveNotesToCloud(notes: List<Note>) {
        notes.forEach {
            cloud.collection("users")
                .document(uid!!)
                .collection("notes")
                .document("${it.rowId}")
                .set(it)
                .addOnSuccessListener {
                    Log.d("TAG", "Successfully!!!")
                }
                .addOnFailureListener {
                    Log.d(REPO_DEBUG, it.message.toString())
                }
        }
    }

    fun updateNoteInCloud(note: Note) {
        cloud.collection("users")
            .document(uid!!)
            .collection("notes")
            .document("${note.rowId}")
            .set(note)
            .addOnSuccessListener {
                Log.d("TAG", "Successfully!!!")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    fun clearDataFromFirebase(notes: List<Note>) {
        notes.forEach {
            cloud.collection("users")
                .document(uid!!)
                .collection("notes")
                .document("${it.rowId}")
                .delete()
                .addOnSuccessListener {
                    Log.d("deleted", "Deleted successed!")
                }
                .addOnFailureListener {
                    Log.d("deleted", "Deleted failure!")
                }
        }
    }

    //TodoList Firebase
    fun saveCategoryToCloud(category: Category) {
        val map = HashMap<String, Category>()
        map.put(category.rowIdCategory.toString(), category)

        cloud.collection("users")
            .document(uid!!)
            .collection("category")
            .document("list")
            .set(map, SetOptions.merge())
//            .addOnSuccessListener {
//                Log.d("TAG", "Successfully!!!")
//            }
//            .addOnFailureListener {
//                Log.d(REPO_DEBUG, it.message.toString())
//            }
    }

    fun deleteCategoryItemsFromFirebase(categories: List<Category>) {
        val map = HashMap<String, Any>()
        categories.forEach {
            map.put(it.rowIdCategory.toString(), FieldValue.delete())
        }

        cloud.collection("users")
            .document(uid!!)
            .collection("category")
            .document("list")
            .update(map)
//            .addOnSuccessListener {
//                Log.d("deleted", "Deleted successed!")
//            }
//            .addOnFailureListener {
//                Log.d("deleted", "Deleted failure!")
//            }
    }

    fun updateCategoriesToCloud(category: List<Category>) {
        val map = HashMap<String, Category>()
        category.forEach { category ->
            map.put(category.rowIdCategory.toString(), category)
        }

        cloud.collection("users")
            .document(uid!!)
            .collection("category")
            .document("list")
            .set(map)
//            .addOnSuccessListener {
//                Log.d("TAG", "Successfully!!!")
//            }
//            .addOnFailureListener {
//                Log.d(REPO_DEBUG, it.message.toString())
//            }
    }

    fun saveTodoListToCloud(categoryId: Int, todoList: List<ItemOfList>) {
        todoList.forEach {
            cloud.collection("users")
                .document(uid!!)
                .collection("todoList")
                .document("$categoryId")
                .collection("listCategory")
                .document("${it.idItem}")
                .set(it)
        }
    }

    //Room Repository
    private val builder = DataBaseBuilder.getInstance(app.applicationContext)
    private val notesDao = builder.notesDao()
    private val categoryDao = builder.categoryDao()
    private val itemDao = builder.itemDao()


    //Note repository
    suspend fun insertNote(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) {
        notesDao.updateNote(note)
    }

    fun deleteNotes(list: List<Note>) {
        notesDao.deleteNotes(list)
    }

    suspend fun deleteOneNote(note: Note?) {
        notesDao.deleteOneNote(note)
    }

    suspend fun clearDatabaseNotes() {
        notesDao.clearDatabaseNotes()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return notesDao.getAllNotes().asLiveData()
    }

//Category repository

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    fun deleteCategory(category: List<Category>) {
        categoryDao.deleteCategory(category)
    }

    suspend fun deleteCategoryDataBase() {
        categoryDao.clearDataBaseCategory()
    }

    fun getAllCategory(): LiveData<List<Category>> {
        return categoryDao.getAllCategory().asLiveData()
    }

//Items repository

    suspend fun insertItem(item: ItemOfList) =
        itemDao.insertItem(item)

    suspend fun deleteItem(item: ItemOfList) =
        itemDao.deleteItem(item)

    suspend fun deleteItems(items: List<ItemOfList>) =
        itemDao.deleteItems(items)


    suspend fun updateItem(item: ItemOfList) =
        itemDao.updateItem(item)

    fun getAllItems(categoryId: Int): LiveData<List<ItemOfList>> {

        val ddd = itemDao.getAllItems(categoryId).asLiveData()
//        val d:LiveData<List<ItemOfList>>
//        CoroutineScope(Dispatchers.IO).launch {
//           val n  =  d
//        }

        return ddd
    }

    fun deleteAllItems(categoryId: Int) =
        itemDao.deleteAllItems(categoryId)

    fun getAllTodoItems(): LiveData<List<ItemOfList>> =
        itemDao.getAllTodoItems().asLiveData()
}