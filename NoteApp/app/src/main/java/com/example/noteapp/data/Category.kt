package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "category_table")
data class Category(
        val categoryName:String,
                val color:String,
                var isSelected:Boolean = false,
                val date:Long){

        @PrimaryKey(autoGenerate = true)
        var rowIdCategory = 0

}
