package com.example.noteapp.tools

import com.example.noteapp.data.Note

interface OnItemClickListener {

    fun onItemClick(note: Note, position: Int)
    fun onItemLongClick(note: Note, position: Int)
}