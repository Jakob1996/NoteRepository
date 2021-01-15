package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    var title:String,
    var message:String,
    val date:Long,
    var isSelected:Boolean = false,
    val color:String,
    val imagePath:String,
    val fontColor: Int,
    val fontSize:Int
    //val isFavourite:Boolean=false,
    //val hasPassword:Boolean=false
) {
    @PrimaryKey(autoGenerate = true)
    var rowId = 0
}