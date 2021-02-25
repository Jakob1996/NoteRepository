package com.example.noteapp.adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.databinding.ItemTodoBinding

class ToDoItemAdapter(private val list: List<ItemOfList>, private val listener: OnItemTodoClickListener): RecyclerView.Adapter<ToDoItemAdapter.ViewHolderItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val holderBinding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderItem(holderBinding)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolderItem(val itemBinding: ItemTodoBinding): RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.todoCheckBox.setOnClickListener {
                listener.onItemClick(list[adapterPosition], adapterPosition)
            }
            itemBinding.todoCheckBox.setOnLongClickListener {
                listener.onItemLongClick(list[adapterPosition], adapterPosition)
                true
            }
        }


        fun bind(todoItem: ItemOfList) {
            itemBinding.todoCheckBox.setText(todoItem.nameItem)

            if (todoItem.isDone) {
                itemBinding.todoCheckBox.isChecked = true
                itemBinding.todoCheckBox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemBinding.todoCheckBox.setTextColor(Color.parseColor("#7B7B7B"))
            } else {
                itemBinding.todoCheckBox.isChecked = false
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
