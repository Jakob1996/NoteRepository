package com.example.noteapp

data class Note (val title:String,
                 val message:String,
                 val date:Long,
                 var isSelected:Boolean){

    val rowId = 0
}
