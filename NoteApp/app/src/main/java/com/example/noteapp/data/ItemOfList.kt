package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "item_table")
data class ItemsOfList(
        @PrimaryKey(autoGenerate = true)
        val idItem: Int = 0,
        val todo:String,
        val categoryName:String)
