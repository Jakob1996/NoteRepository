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
    suspend fun updateCategoty(category: Category)

    @Delete
    suspend fun deleteCategory(category:List<Category>)

    @Query("SELECT * FROM category_table  ORDER BY date DESC")
    fun getAllCategoryItems(): Flow<List<Category>>

    @Query("DELETE FROM category_table")
    suspend fun clearDataBaseCategory()
}