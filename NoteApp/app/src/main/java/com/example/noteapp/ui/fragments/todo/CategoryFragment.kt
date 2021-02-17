package com.example.noteapp.ui.fragments.todo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.ItemsCategoryTodoAdapter
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemCategoryClickListener
import com.example.noteapp.data.Category
import com.example.noteapp.ui.fragments.note.RemovePasswordDialogFragment
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_to_do.*
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.android.synthetic.main.note_edit_layout_miscellaneous.*
import kotlinx.android.synthetic.main.todo_layout_miscellaneous.*

class CategoryFragment : Fragment(), OnItemCategoryClickListener,
    SortDialogFragment.OnItemClickDialogListener {
    private lateinit var viewModel: ViewModel
    private lateinit var toDoCategoryAdapter: ItemsCategoryTodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category_recyclerView.layoutManager = StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL)
        checkIsEmpty()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getSortDescCategory().observe(requireActivity(), Observer {
            updateItems(viewModel.allCategoryItems.value!!)
        })


        viewModel.getCategoryFabButtonMode().observe(requireActivity(), Observer {
            /*
            if(it==true){
                updateItems(viewModel.allCategoryItems.value!!.filter {
                    it.isFavoutire == true
                })
            } else{
                updateItems(viewModel.allCategoryItems.value!!)
            }
             */
            updateItems(viewModel.allCategoryItems.value!!)
        })

        viewModel.getNotifyDataCategory().observe(viewLifecycleOwner, Observer {
            if(it==true){
                viewModel.allCategoryItems.value?.forEach { it.isSelected = false }
                updateItems(viewModel.allCategoryItems.value!!)
                exitMultiSelectMode()
                viewModel.setNotifyDataCategory(false)
            }
        })

        viewModel.allCategoryItems.observe(viewLifecycleOwner, Observer {

            viewModel.allCategoryItems.value?.forEach { for (i in viewModel.selectedCategoryItems){ if(it.rowIdCategory.equals(i.rowIdCategory)){
                it.isSelected=true
            }} }

            updateItems(it)
        })
    }

    override fun onItemClick(category:Category, position: Int) {
        if(viewModel.getMultiSelectMode().value == true){
            if(viewModel.selectedCategoryItems.contains(category)){
                unselectCategoryItem(category, position)
            } else{
                selectCategoryItem(category, position)
            }
        } else {
            viewModel.setSelectedCategotyItem(category)
            viewModel.categoryItemBeforeChange = category
            if(category.hasPassword){
                findNavController().navigate(R.id.action_mainFramgent_to_checkPasswordFragment)
            } else{
                findNavController().navigate(R.id.action_mainFramgent_to_addEditToDoFragment)
            }
        }
    }

    override fun onItemLongClick(category: Category, position: Int) {
        if(viewModel.getMultiSelectMode().value == false){
            viewModel.setMutliSelectMode(true)
            selectCategoryItem(category, position)
        }
    }

    private fun selectCategoryItem(item: Category, position: Int) {
        item.isSelected = true
        viewModel.selectedCategoryItems.add(item)
        toDoCategoryAdapter.notifyItemChanged(position)
    }

    private fun unselectCategoryItem(item: Category, position: Int) {
        viewModel.selectedCategoryItems.remove(item)

        item.isSelected = false

        toDoCategoryAdapter.notifyItemChanged(position)

        if(viewModel.selectedCategoryItems.isEmpty()&&viewModel.selectedNotes.isEmpty()) {
            exitMultiSelectMode()
        }
    }

    private fun exitMultiSelectMode() {

        viewModel.selectedNotes.forEach { it.isSelected = false }
        viewModel.selectedNotes.clear()

        viewModel.selectedCategoryItems.forEach{it.isSelected = false}
        viewModel.selectedCategoryItems.clear()

        viewModel.setMutliSelectMode(false)
    }

    private fun updateItems(list:List<Category>) {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        category_recyclerView.layoutManager = lm

        val listMode:List<Category>

        if(viewModel.getCategoryFabButtonMode().value==true){
            listMode = list.filter { it.isFavoutire==true }
        } else {
            listMode = list
        }

        if(viewModel.getSortDescCategory().value!=null) {
            toDoCategoryAdapter = if (viewModel.getSortDescCategory().value!!) {
                ItemsCategoryTodoAdapter(listMode, this)

            } else {
                ItemsCategoryTodoAdapter(listMode.asReversed(), this)
            }
        } else{
            toDoCategoryAdapter = if (viewModel.p) {
                ItemsCategoryTodoAdapter(listMode, this)
            } else {
                ItemsCategoryTodoAdapter(listMode.asReversed(), this)
            }
        }

/*
        toDoCategoryAdapter = if(viewModel.getSortDescNote().value!!) {
        } else{
            ItemsCategoryTodoAdapter(list.asReversed(), this) // asReversed - Na odwrót
        }

 */
        category_recyclerView.adapter = toDoCategoryAdapter

        if(viewModel.categoryToDoState!=null){
            (category_recyclerView.layoutManager as StaggeredGridLayoutManager).onRestoreInstanceState(viewModel.categoryToDoState)
        }

        checkIsEmpty()
        toDoCategoryAdapter.notifyDataSetChanged()
    }

    private fun checkIsEmpty() {
        viewModel.allCategoryItems.observe(viewLifecycleOwner, Observer {
            if(viewModel.allCategoryItems.value!!.size==0){
                empty_textView_Todo.visibility = View.VISIBLE
            } else{
                empty_textView_Todo.visibility = View.GONE
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.categoryToDoState = category_recyclerView.layoutManager?.onSaveInstanceState()
    }
}

