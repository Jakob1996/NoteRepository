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
        val imagePaths:ArrayList<String> = arrayListOf("bbb"),
        val fontColor:Int=1,
        val fontSize:Int=1,
        val isFavourite:Boolean=false,
        val hasPassword:Boolean=false,
        val password:Int=0,
) {
    @PrimaryKey(autoGenerate = true)
    var rowId = 0
}

class PathTypeConverter{

    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListLisr(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}