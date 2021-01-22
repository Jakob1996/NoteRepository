package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    var title:String="",
    var message:String="",
    val date:Long=1,
    var isSelected:Boolean = false,
    val color:String="",
    val imagePath:String="",
    val fontColor:Int=1,
    val fontSize:Int=1,
    val isFavourite:Boolean=false,
    val hasPassword:Boolean=false,
    val password:Int=0
) {
    @PrimaryKey(autoGenerate = true)
    var rowId = 0
}