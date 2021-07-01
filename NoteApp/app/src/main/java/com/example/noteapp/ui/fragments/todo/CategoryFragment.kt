package com.example.noteapp.ui.fragments.todo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.ItemsCategoryTodoAdapter
import com.example.noteapp.adapters.OnItemCategoryClickListener
import com.example.noteapp.data.Category
import com.example.noteapp.databinding.CategoryItemBinding
import com.example.noteapp.databinding.FragmentTodoListBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ToDoViewModel

class CategoryFragment : Fragment(), OnItemCategoryClickListener,
    SortDialogFragment.OnItemClickDialogListener, Navigation {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: ToDoViewModel
    private lateinit var toDoCategoryAdapter: ItemsCategoryTodoAdapter

    private var _binding:FragmentTodoListBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("hh", "onCreateCategoryFrag")

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryRecyclerView.layoutManager = StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL)
        checkIsEmpty()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        noteViewModel.getSortDescCategory().observe(requireActivity(), {
            updateItems(todoViewModel.allCategoryItems.value!!)
        })

        noteViewModel.getCategoryFabButtonMode().observe(requireActivity(), {
            updateItems(todoViewModel.allCategoryItems.value!!)
            if(todoViewModel.allCategoryItems.value!!.none { it.isFavoutire }
                &&noteViewModel.getNoteFabButtonMode().value ==true){
                binding.fragmentTodoEmptyFavouriteTv.visibility = View.VISIBLE
            } else{
                binding.fragmentTodoEmptyFavouriteTv.visibility = View.INVISIBLE
            }
        })

        noteViewModel.getNotifyDataCategory().observe(viewLifecycleOwner, {
            if(it==true){
                todoViewModel.allCategoryItems.value?.forEach { it.isSelected = false }
                updateItems(todoViewModel.allCategoryItems.value!!)
                exitMultiSelectMode()
                noteViewModel.setNotifyDataCategory(false)
            }
        })

        todoViewModel.allCategoryItems.observe(viewLifecycleOwner, {

            todoViewModel.allCategoryItems.value?.forEach { for (i in todoViewModel.selectedCategoryItems){ if(it.rowIdCategory.equals(i.rowIdCategory)){
                it.isSelected=true
            }} }

            updateItems(it)
        })
    }

    override fun onItemClick(category:Category, position: Int) {
        if(noteViewModel.getMultiSelectMode().value == true){
            if(todoViewModel.selectedCategoryItems.contains(category)){
                unselectCategoryItem(category, position)
            } else{
                selectCategoryItem(category, position)
            }
        } else {
            todoViewModel.setSelectedCategotyItem(category)
            todoViewModel.categoryItemBeforeChange = category
            if(category.hasPassword){

            } else{
                navigateToFragment(AddEditToDoFragment(), "CatFrag", requireActivity().supportFragmentManager)
            }
        }
    }

    override fun onItemLongClick(category: Category, position: Int) {
        if(noteViewModel.getMultiSelectMode().value == false){
            noteViewModel.setMutliSelectMode(true)
            selectCategoryItem(category, position)
        }
    }

    private fun selectCategoryItem(item: Category, position: Int) {
        item.isSelected = true
        todoViewModel.selectedCategoryItems.add(item)
        toDoCategoryAdapter.notifyItemChanged(position)
    }

    private fun unselectCategoryItem(item: Category, position: Int) {
        todoViewModel.selectedCategoryItems.remove(item)

        item.isSelected = false

        toDoCategoryAdapter.notifyItemChanged(position)

        if(todoViewModel.selectedCategoryItems.isEmpty()&&noteViewModel.selectedNotes.isEmpty()) {
            exitMultiSelectMode()
        }
    }

    private fun exitMultiSelectMode() {

        noteViewModel.selectedNotes.forEach { it.isSelected = false }
        noteViewModel.selectedNotes.clear()

        todoViewModel.selectedCategoryItems.forEach{it.isSelected = false}
        todoViewModel.selectedCategoryItems.clear()

        noteViewModel.setMutliSelectMode(false)
    }

    private fun updateItems(list:List<Category>) {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.categoryRecyclerView.layoutManager = lm

        Log.d("hh", "updateCategory")

        val listMode:List<Category>

        if(noteViewModel.getCategoryFabButtonMode().value==true){
            listMode = list.filter { it.isFavoutire==true }
        } else {
            listMode = list
        }

        if(noteViewModel.getSortDescCategory().value!=null) {
            toDoCategoryAdapter = if (noteViewModel.getSortDescCategory().value!!) {
                ItemsCategoryTodoAdapter(listMode, this)

            } else {
                ItemsCategoryTodoAdapter(listMode.asReversed(), this)
            }
        } else{
            toDoCategoryAdapter = if (noteViewModel.p) {
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
        binding.categoryRecyclerView.adapter = toDoCategoryAdapter

        if(noteViewModel.categoryToDoState!=null){
            (binding.categoryRecyclerView.layoutManager as StaggeredGridLayoutManager).onRestoreInstanceState(noteViewModel.categoryToDoState)
        }

        checkIsEmpty()
        toDoCategoryAdapter.notifyDataSetChanged()
    }

    private fun checkIsEmpty() {
        todoViewModel.allCategoryItems.observe(viewLifecycleOwner, {
            if(todoViewModel.allCategoryItems.value!!.isEmpty()){
                binding.emptyTextViewTodo.visibility = View.VISIBLE
            } else{
                binding.emptyTextViewTodo.visibility = View.GONE
            }
        })
    }

    override fun onPause() {
        super.onPause()
        noteViewModel.categoryToDoState = binding.categoryRecyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroyView() {

        _binding = null
        super.onDestroyView()
    }
}

