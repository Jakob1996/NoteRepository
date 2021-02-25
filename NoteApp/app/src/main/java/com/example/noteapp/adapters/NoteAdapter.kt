package com.example.noteapp.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.NoteItemBinding

class NoteAdapter(private val noteList:List<Note>, private val listener: OnItemClickListener): RecyclerView.Adapter <NoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = noteList[position]

        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class MyViewHolder(private val viewBinding:NoteItemBinding):RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.linear.setOnClickListener{
                listener.onItemClick(noteList[adapterPosition], adapterPosition)
            }
            viewBinding.linear.setOnLongClickListener{
                listener.onItemLongClick(noteList[adapterPosition], adapterPosition)
                true
            }
        }

        @SuppressLint("Range")
        fun bind(note:Note){
            viewBinding.linear.setBackgroundColor(Color.parseColor(note.color))

            if(note.isSelected){
                viewBinding.linear.setBackgroundColor(Color.parseColor("#F0F4D7"))
            }

            if(note.isFavourite){

                viewBinding.favLayItem.visibility = View.VISIBLE
                viewBinding.itemsLayout.visibility = View.VISIBLE
            } else{
                viewBinding.favLayItem.visibility = View.GONE
            }

            if (note.hasPassword){
                viewBinding.delLayItem.visibility = View.VISIBLE
                viewBinding.itemsLayout.visibility = View.VISIBLE
            } else{
                viewBinding.delLayItem.visibility = View.GONE
            }

            if(!note.hasPassword&&!note.isFavourite){
                viewBinding.itemsLayout.visibility = View.GONE
            }

            viewBinding.noteTitle.setText(note.title)
            viewBinding.noteMessage.setText(note.message)
        }
    }
}

interface OnItemClickListener{
    fun onItemClick(note: Note, position:Int)
    fun onItemLongClick(note: Note, position: Int)
}
