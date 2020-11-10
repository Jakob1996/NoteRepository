package com.example.noteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val notes:List<Note>): RecyclerView.Adapter <NoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.note_title

    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view) {
    }
}