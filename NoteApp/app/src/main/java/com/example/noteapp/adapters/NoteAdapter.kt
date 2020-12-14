package com.example.noteapp.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_note.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val noteList:List<Note>, private val listener: OnItemClickListener): RecyclerView.Adapter <NoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val imageNote = holder.itemView.findViewById<ImageView>(R.id.imageNote)
        holder.itemView.linear.setBackgroundColor(Color.parseColor(noteList[position].color))
        if(noteList[position].isSelected){
            Log.d("addda", "in")
            holder.itemView.linear.setBackgroundColor(Color.parseColor("#F0F4D7"))
        }

        Log.d("addda", "out")
        holder.itemView.note_title.text = noteList[position].title
        holder.itemView.note_message.text = noteList[position].message

        if(!noteList[position].imagePath.isEmpty()){
            imageNote.setImageBitmap(BitmapFactory.decodeFile(noteList[position].imagePath))
            imageNote.visibility = View.VISIBLE
        } else{
            imageNote.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        Log.d("sizeaaa", "${noteList.size}")
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
