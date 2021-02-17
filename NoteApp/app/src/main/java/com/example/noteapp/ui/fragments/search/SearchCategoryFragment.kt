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
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_search.icBackInSearchFragment
import kotlinx.android.synthetic.main.fragment_search_category.*

class SearchCategoryFragment : Fragment(), OnItemCategoryClickListener {

    private lateinit var viewModel: ViewModel
    private lateinit var categoryAdapter: ItemsCategoryTodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.search = null
                findNavController().navigate(R.id.action_searchCategoryFragment_to_mainFramgent)
                isEnabled = false
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allCategoryItems.observe(viewLifecycleOwner, Observer {
            if(viewModel.search!=null){
                updateCategory(it, viewModel.search!!)
            } else{
                updateCategoryEmpty()
            }
        })

        if(viewModel.search!=null) {
            editSearcherCategory.setText(viewModel.search)
            editSearcherCategory.requestFocus(editSearcherCategory.text.length)
        }

        icBackInSearchFragment.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_searchFragment_to_mainFramgent)
            }
        })

        editSearcherCategory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(!s.isNullOrEmpty()) {
                    updateCategory(viewModel.allCategoryItems.value!!, s.toString())
                    viewModel.search = editSearcherCategory.text.toString()
                } else updateCategoryEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        } )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

    }

    private fun updateCategory(list:List<Category>, search:String) {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_InSearch_Category.layoutManager = lm


        val listMod = list.filter {  it ->
            it.categoryName.toLowerCase().contains(search.toLowerCase())
        }

        categoryAdapter = ItemsCategoryTodoAdapter(listMod, this)
        recyclerView_InSearch_Category.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }

    private fun updateCategoryEmpty() {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        recyclerView_InSearch_Category.layoutManager = lm

        categoryAdapter = ItemsCategoryTodoAdapter(emptyList(), this)
        recyclerView_InSearch_Category.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(category: Category, position: Int) {
        viewModel.setSelectedCategotyItem(category)
        viewModel.categoryItemBeforeChange = category
        if(category.hasPassword){
            viewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchCategoryFragment_to_checkPasswordFragment)
        } else
        {   viewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchCategoryFragment_to_addEditToDoFragment)
        }
    }

    override fun onItemLongClick(category: Category, position: Int) {

    }
}