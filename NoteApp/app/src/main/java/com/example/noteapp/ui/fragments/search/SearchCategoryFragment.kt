package com.example.noteapp.ui.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.ItemsCategoryTodoAdapter
import com.example.noteapp.adapters.OnItemCategoryClickListener
import com.example.noteapp.data.Category
import com.example.noteapp.databinding.FragmentSearchCategoryBinding
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.viewmodels.ToDoViewModel

class SearchCategoryFragment: BaseFragment(), OnItemCategoryClickListener {

    private lateinit var todoViewModel: ToDoViewModel
    private lateinit var categoryAdapter: ItemsCategoryTodoAdapter

    private var _binding:FragmentSearchCategoryBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                todoViewModel.search = null
                popBackStack("SCF", requireActivity().supportFragmentManager, false)
                isEnabled = false
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPress() {
        //TODO
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSearchCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoViewModel.allCategoryItems.observe(viewLifecycleOwner, Observer {
            if(todoViewModel.search!=null){
                updateCategory(it, todoViewModel.search!!)
            } else{
                updateCategoryEmpty()
            }
        })

        if(todoViewModel.search!=null) {
            binding.editSearcherCategory.setText(todoViewModel.search)
            binding.editSearcherCategory.requestFocus(binding.editSearcherCategory.text.length)
        }

        binding.icBackInSearchFragment.setOnClickListener{
            findNavController().navigate(R.id.action_searchFragment_to_mainFramgent)
        }

        binding.editSearcherCategory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(!s.isNullOrEmpty()) {
                    updateCategory(todoViewModel.allCategoryItems.value!!, s.toString())
                    todoViewModel.search = binding.editSearcherCategory.text.toString()
                } else updateCategoryEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        } )
    }

    private fun updateCategory(list:List<Category>, search:String) {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewInSearchCategory.layoutManager = lm


        val listMod = list.filter {  it ->
            it.categoryName.toLowerCase().contains(search.toLowerCase())
        }

        categoryAdapter = ItemsCategoryTodoAdapter(listMod, this)
        binding.recyclerViewInSearchCategory.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }

    private fun updateCategoryEmpty() {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerViewInSearchCategory.layoutManager = lm

        categoryAdapter = ItemsCategoryTodoAdapter(emptyList(), this)
        binding.recyclerViewInSearchCategory.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(category: Category, position: Int) {
        todoViewModel.setSelectedCategotyItem(category)
        todoViewModel.categoryItemBeforeChange = category
        if(category.hasPassword){
            todoViewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchCategoryFragment_to_checkPasswordFragment)
        } else
        {   todoViewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchCategoryFragment_to_addEditToDoFragment)
        }
    }

    override fun onItemLongClick(category: Category, position: Int) {

    }
}