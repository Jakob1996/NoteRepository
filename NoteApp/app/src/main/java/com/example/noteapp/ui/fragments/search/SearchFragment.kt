package com.example.noteapp.ui.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(), OnItemClickListener {

    private lateinit var viewModel: ViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(this, object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("onDDD", "oBP")
                viewModel.search = null
                findNavController().navigate(R.id.action_searchFragment_to_mainFramgent)
                isEnabled = false
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            if(viewModel.search!=null){
                updateNotes(it, viewModel.search!!)
            } else{
              updateNotesEmpty()
            }
        })

        if(viewModel.search!=null) {
            editSearcher.setText(viewModel.search)
            editSearcher.requestFocus(editSearcher.text.length)
        }

        icBackInSearchFragment.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_searchFragment_to_mainFramgent)
            }
        })

        editSearcher.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(!s.isNullOrEmpty()) {
                    updateNotes(viewModel.allNotes.value!!, s.toString())
                    viewModel.search = editSearcher.text.toString()
                } else updateNotesEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        } )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
    }

    private fun updateNotes(list:List<Note>, search:String) {
        val lm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recyclerView_InSearch.layoutManager = lm

        val listMod = list.filter {  it ->
            it.title.toLowerCase().contains(search.toLowerCase()) || it.message.toUpperCase().contains(search.toUpperCase())
        }

        noteAdapter = NoteAdapter(listMod, this)
        recyclerView_InSearch.adapter = noteAdapter
        noteAdapter.notifyDataSetChanged()
    }

    private fun updateNotesEmpty() {
        val lm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recyclerView_InSearch.layoutManager = lm

        noteAdapter = NoteAdapter(emptyList(), this)
        recyclerView_InSearch.adapter = noteAdapter
        noteAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(note: Note, position: Int) {
        viewModel.setSelectedNote(note)
        viewModel.noteBeforeChange = note
        if(note.hasPassword){
            viewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchFragment_to_checkPasswordFragment)
        } else
        {   viewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchFragment_to_beforeAddEditNoteFragment)
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {

    }
}