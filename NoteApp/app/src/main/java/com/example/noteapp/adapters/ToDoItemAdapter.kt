package com.example.noteapp.adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.databinding.TodoItemBinding

class ToDoItemAdapter(
    private val list: List<ItemOfList>,
    private val listener: OnItemTodoClickListener
) : RecyclerView.Adapter<ToDoItemAdapter.ViewHolderItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val holderBinding =
            TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderItem(holderBinding)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolderItem(val itemBinding: TodoItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.todoItemCb.setOnClickListener {
                listener.onItemClick(list[bindingAdapterPosition], bindingAdapterPosition)
            }
            itemBinding.todoItemCb.setOnLongClickListener {
                listener.onItemLongClick(list[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
        }


        fun bind(todoItem: ItemOfList) {
            itemBinding.todoItemCb.setText(todoItem.nameItem)

            if (todoItem.isDone) {
                itemBinding.todoItemCb.isChecked = true
                itemBinding.todoItemCb.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemBinding.todoItemCb.setTextColor(Color.parseColor("#7B7B7B"))
            } else {
                itemBinding.todoItemCb.isChecked = false
            }
        }
    }

    fun getItemInPosition(position: Int): ItemOfList {
        return list[position]
    }

}

interface OnItemTodoClickListener {
    fun onItemClick(itemsOfList: ItemOfList, position: Int)
    fun onItemLongClick(itemsOfList: ItemOfList, position: Int)
}
