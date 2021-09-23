package com.example.noteapp.db

import androidx.room.*
import com.example.noteapp.data.Category
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {

    //Category Dao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: List<Category>)

    @Query("SELECT * FROM category_table  ORDER BY date DESC")
    fun getAllCategory(): Flow<List<Category>>

    @Query("DELETE FROM category_table")
    fun clearDataBaseCategory()
}