package com.example.noteapp.adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.ui.fragments.todo.AddEditToDoFragment
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_add_item_dialog.view.*
import kotlinx.android.synthetic.main.item_todo.view.*

class ToDoItemAdapter(private val list: List<ItemOfList>, private val listener: OnItemTodoClickListener): RecyclerView.Adapter<ToDoItemAdapter.ViewHolderItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_todo, parent, false)
        return ViewHolderItem(view)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {

        holder.itemView.todoCheckBox.setText(list[position].nameItem)

        if(list[position].isDone){
            holder.itemView.todoCheckBox.isChecked = true
            holder.itemView.todoCheckBox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.todoCheckBox.setTextColor(Color.parseColor("#7B7B7B"))
        } else{
            holder.itemView.todoCheckBox.isChecked = false
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolderItem(view: View): RecyclerView.ViewHolder(view){
        init {
            view.todoCheckBox.setOnClickListener{
                listener.onItemClick(list[adapterPosition], adapterPosition)
            }
            view.todoCheckBox.setOnLongClickListener{
                listener.onItemLongClick(list[adapterPosition], adapterPosition)
                true
            }
        }
    }

    fun getItemInPosition(position: Int):ItemOfList{
        return list.get(position)
    }

}

interface OnItemTodoClickListener{
    fun onItemClick(itemsOfList: ItemOfList, position:Int)
    fun onItemLongClick(itemsOfList: ItemOfList, position: Int)
}
