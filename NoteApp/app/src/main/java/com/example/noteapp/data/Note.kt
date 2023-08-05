package com.example.noteapp.data

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "note_table")
data class Note(
        var title:String="",
        var message:String="",
        val date:Long=1,
        var isSelected:Boolean = false,
        val color:String="",
        val fontColor:Int=1,
        val fontSize:Int=1,
        val isFavourite:Boolean=false,
        val hasPassword:Boolean=false,
        val password:Int=0,
        val imagePaths:String
        ) {
    @PrimaryKey(autoGenerate = true)
    var rowId = 0
}


class PathTypeConverter{
    companion object {
        @TypeConverter
        fun fromJsonToPathList(value: String): List<String> {
            val listType = object : TypeToken<List<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun toJsonPathList(list: List<String>): String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}