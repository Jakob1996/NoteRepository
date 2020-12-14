package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note (val title:String,
                 val message:String,
                 val date:Long,
                 var isSelected:Boolean = false,
                 val color:String,
                val imagePath:String){

    @PrimaryKey(autoGenerate = true)
    var rowId = 0

    override fun toString(): String {
        return "$title, $message, $date, $isSelected, $color $rowId"
    }
}
