package com.example.noteapp.ui.fragments.todo

import android.os.Bundle
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
import com.example.noteapp.adapters.OnItemCategoryClickListener
import com.example.noteapp.data.Category
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_to_do.*
import kotlinx.android.synthetic.main.fragment_todo_list.*

class CategoryFragment : Fragment(), OnItemCategoryClickListener,
    SortDialogFragment.OnItemClickDialogListener {
    private lateinit var viewModel: ViewModel
    private lateinit var toDoCategoryAdapter: ItemsCategoryTodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        viewModel.setSelectedCategotyItem(null)
        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(viewModel.getMultiSelectCategoryMode().value==true ){
                    exitMultiSelectMode()
                } else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
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

        viewModel.allCategoryItems.observe(viewLifecycleOwner, Observer {
            updateItems(it)

            viewModel.allCategoryItems.value?.forEach { for (i in viewModel.selectedCategoryItems){ if(it.rowIdCategory.equals(i.rowIdCategory)){
                it.isSelected=true
            }} }

            updateItems(it)
        })
    }

    override fun onItemClick(category:Category, position: Int) {
        if(viewModel.getMultiSelectCategoryMode().value == true){
            if(viewModel.selectedCategoryItems.contains(category)){
                unselectCategoryItem(category, position)
            } else{
                selectCategoryItem(category, position)
            }
        } else {
            viewModel.setSelectedCategotyItem(category)
            findNavController().navigate(R.id.action_mainFramgent_to_addEditToDoFragment)
        }
    }

    override fun onItemLongClick(category: Category, position: Int) {
        if(viewModel.getMultiSelectCategoryMode().value == false){
            viewModel.setMutliSelectCategoryMode(true)
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

        if(viewModel.selectedCategoryItems.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        viewModel.setMutliSelectCategoryMode(false)
        viewModel.selectedCategoryItems.forEach{it.isSelected = false}
        viewModel.selectedCategoryItems.clear()

        toDoCategoryAdapter.notifyDataSetChanged()
    }

    private fun updateItems(list:List<Category>) {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        category_recyclerView.layoutManager = lm

        toDoCategoryAdapter = if(viewModel.sortDescNote) {
            ItemsCategoryTodoAdapter(list, this)
        } else{
            ItemsCategoryTodoAdapter(list.asReversed(), this) // asReversed - Na odwrót
        }

        category_recyclerView.adapter = toDoCategoryAdapter

        checkIsEmpty()
        toDoCategoryAdapter.notifyDataSetChanged()
    }

    private fun checkIsEmpty() {
        viewModel.allCategoryItems.observe(viewLifecycleOwner, Observer {
            if(viewModel.allCategoryItems.value!!.size==0){7
                empty_textView_Todo.visibility = View.VISIBLE
            } else{
                empty_textView_Todo.visibility = View.GONE
            }
        })
    }
}

