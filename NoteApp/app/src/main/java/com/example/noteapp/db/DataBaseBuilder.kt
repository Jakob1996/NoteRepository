package com.example.noteapp.db

import android.content.Context
import androidx.room.Room

object DataBaseBuilder {

    private var instance:ToDoDataBase? = null

    fun getInstance(context:Context): ToDoDataBase{
        if(instance==null){
            synchronized(ToDoDataBase::class){
                instance = roomBuild(context)
            }
        }
        return instance!!
    }

    private fun roomBuild(context: Context) =
        Room.databaseBuilder(context.applicationContext, ToDoDataBase::class.java, "todo_database").build()
}