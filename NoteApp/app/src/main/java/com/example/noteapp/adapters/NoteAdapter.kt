package com.example.noteapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.Note
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val noteList:List<Note>, private val listener: OnItemClickListener): RecyclerView.Adapter <NoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(Color.WHITE)
        if(noteList[position].isSelected){
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        }

        holder.itemView.note_title.text = noteList[position].title
        holder.itemView.note_message.text = noteList[position].message
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener{
                listener.onItemClick(noteList[adapterPosition], adapterPosition)
            }
            view.setOnLongClickListener{
                listener.onItemLongClick(noteList[adapterPosition], adapterPosition)
                true
            }
        }
    }
}

interface OnItemClickListener{
    fun onItemClick(note: Note, position:Int)
    fun onItemLongClick(note: Note, position: Int)
}