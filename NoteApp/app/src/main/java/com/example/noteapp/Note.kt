package com.example.noteapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note (val title:String,
                 val message:String,
                 val date:Long,
                 var isSelected:Boolean){

    @PrimaryKey(autoGenerate = true)
    var rowId = 0
}
