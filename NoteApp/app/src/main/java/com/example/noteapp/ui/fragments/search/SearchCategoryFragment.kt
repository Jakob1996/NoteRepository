package com.example.noteapp.ui.fragments.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.example.noteapp.viewmodels.TodoViewModel

class SearchCategoryFragment : BaseFragment(), OnItemCategoryClickListener {

    private lateinit var todoViewModel: TodoViewModel

    private lateinit var categoryAdapter: ItemsCategoryTodoAdapter

    private var _binding: FragmentSearchCategoryBinding? = null

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    todoViewModel.setIsFromMainFragmentNavigation(true)
                    todoViewModel.search = null
                    popBackStack("SCF", requireActivity().supportFragmentManager, false)
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSoftKeyboard()

        binding.fragmentSearchCategorySearchEt.requestFocus()

        todoViewModel.allCategoryItems.observe(viewLifecycleOwner, {
            if (todoViewModel.search != null) {
                updateCategory(it, todoViewModel.search!!)
            } else {
                updateCategoryEmpty()
            }
        })

        if (todoViewModel.search != null) {
            binding.fragmentSearchCategorySearchEt.setText(todoViewModel.search)
            binding.fragmentSearchCategorySearchEt.requestFocus(binding.fragmentSearchCategorySearchEt.text.length)
        }

        binding.fragmentSearchCategoryToolbarBackIv.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.fragmentSearchCategorySearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (!s.isNullOrEmpty()) {
                    updateCategory(todoViewModel.allCategoryItems.value!!, s.toString())
                    todoViewModel.search = binding.fragmentSearchCategorySearchEt.text.toString()
                } else updateCategoryEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateCategory(list: List<Category>, search: String) {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.fragmentSearchCategoryRv.layoutManager = lm


        val listMod = list.filter {
            it.categoryName.toLowerCase().contains(search.toLowerCase())
        }

        categoryAdapter = ItemsCategoryTodoAdapter(listMod, this)
        binding.fragmentSearchCategoryRv.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }

    private fun updateCategoryEmpty() {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        binding.fragmentSearchCategoryRv.layoutManager = lm

        categoryAdapter = ItemsCategoryTodoAdapter(emptyList(), this)
        binding.fragmentSearchCategoryRv.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(category: Category, position: Int) {
        todoViewModel.setSelectedCategotyItem(category)
        todoViewModel.categoryItemBeforeChange = category
        if (category.hasPassword) {
            todoViewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_search_category_fragment_to_check_password_fragment)
        } else {
            todoViewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_search_category_fragment_to_general_todo_fragment)
        }
    }

    override fun onItemLongClick(category: Category, position: Int) {
        //NOT YET!
    }
}