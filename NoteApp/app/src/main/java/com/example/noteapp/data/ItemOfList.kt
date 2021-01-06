package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class ItemOfList(
        val nameItem:String,
        val categoryId:Int,
        var isDone:Boolean=false){

        @PrimaryKey(autoGenerate = true)
        var idItem = 0
}