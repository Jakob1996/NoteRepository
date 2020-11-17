package com.example.noteapp.db

import androidx.room.*
import com.example.noteapp.data.Note
import kotlinx.coroutines.flow.Flow

//Dao "Data access object"
@Dao
interface NoteDao {

    //suspend - asynchronicznosc, zawieszenie. Piszemy to po to aby komputer
    // wiedział aby nie wykonywac tego na wątku głównym

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun deleteNotes(notes:List<Note>)

    @Query("SELECT*FROM NOTE_TABLE ORDER BY date DESC" )
    fun getAllNotes():Flow<List<Note>>
}