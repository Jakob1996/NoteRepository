package com.example.noteapp.db

import androidx.room.*
import com.example.noteapp.data.ItemsOfList
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    //Item Dao
    @Insert
    suspend fun insertItem(item: ItemsOfList)

    @Delete
    suspend fun deleteItems(items:List<ItemsOfList>)

    @Delete
    suspend fun deleteItem(item: ItemsOfList)

    @Update
    suspend fun updateItem(item: ItemsOfList)

    @Query ("SELECT * FROM ITEM_TABLE WHERE categoryName = :categoryName")
    fun getAllItems(categoryName: String):Flow<List<ItemsOfList>>

    @Query ("DELETE FROM ITEM_TABLE WHERE categoryName = :categoryName")
    suspend fun deleteAllItems(categoryName: String)
}