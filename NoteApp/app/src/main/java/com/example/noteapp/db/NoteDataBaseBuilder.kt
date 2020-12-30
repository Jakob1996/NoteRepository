package com.example.noteapp.db

import android.content.Context
import androidx.room.Room

object NoteDataBaseBuilder {

    private var instance:NoteDataBase? = null

    fun getInstance(context: Context): NoteDataBase {
        if(instance==null){
            synchronized(NoteDataBase::class){
                instance = roomBuild(context)
            }
        }
        return instance!!
    }

    private fun roomBuild(context: Context) =
        Room.databaseBuilder(context.applicationContext, NoteDataBase::class.java, "note_database")
                .build()
    // Pozwalamy na niszczenie starej bazy,
    // jeżeli pojawi się nowa wersja i tworzy nową wersje

}