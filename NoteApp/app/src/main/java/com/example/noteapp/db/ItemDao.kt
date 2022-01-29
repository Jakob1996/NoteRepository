package com.example.noteapp.db

import androidx.room.*
import com.example.noteapp.data.ItemOfList
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    //Item Dao
    @Insert
    suspend fun insertItem(item: ItemOfList)

    @Delete
    suspend fun deleteItems(items: List<ItemOfList>)

    @Delete
    suspend fun deleteItem(item: ItemOfList)

    @Update
    suspend fun updateItem(item: ItemOfList)

    @Query("SELECT * FROM item_table WHERE categoryId = :categoryId")
    fun getAllItems(categoryId: Int): Flow<List<ItemOfList>>

    @Query("DELETE FROM item_table WHERE categoryId = :categoryId")
    fun deleteAllItems(categoryId: Int)

    @Query("SELECT * FROM item_table")
    fun getAllTodoItems(): Flow<List<ItemOfList>>
}