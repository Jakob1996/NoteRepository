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
import com.example.noteapp.databinding.FragmentSearchBinding
import com.example.noteapp.viewmodels.NoteViewModel

class SearchFragment : Fragment(), OnItemClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    private var _binding:FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(this, object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("onDDD", "oBP")
                noteViewModel.search = null
                findNavController().navigate(R.id.action_searchFragment_to_mainFramgent)
                isEnabled = false
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            if(noteViewModel.search!=null){
                updateNotes(it, noteViewModel.search!!)
            } else{
              updateNotesEmpty()
            }
        })

        if(noteViewModel.search!=null) {
            binding.editSearcher.setText(noteViewModel.search)
            binding.editSearcher.requestFocus(binding.editSearcher.text.length)
        }

        binding.icBackInSearchFragment.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_searchFragment_to_mainFramgent)
            }
        })

        binding.editSearcher.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(!s.isNullOrEmpty()) {
                    updateNotes(noteViewModel.allNotes.value!!, s.toString())
                    noteViewModel.search = binding.editSearcher.text.toString()
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

        binding.recyclerViewInSearch.layoutManager = lm

        val listMod = list.filter {  it ->
            it.title.toLowerCase().contains(search.toLowerCase()) || it.message.toUpperCase().contains(search.toUpperCase())
        }

        noteAdapter = NoteAdapter(listMod, this)
        binding.recyclerViewInSearch.adapter = noteAdapter
        noteAdapter.notifyDataSetChanged()
    }

    private fun updateNotesEmpty() {
        val lm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerViewInSearch.layoutManager = lm

        noteAdapter = NoteAdapter(emptyList(), this)
        binding.recyclerViewInSearch.adapter = noteAdapter
        noteAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(note: Note, position: Int) {
        noteViewModel.setSelectedNote(note)
        noteViewModel.noteBeforeChange = note
        if(note.hasPassword){
            noteViewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchFragment_to_checkPasswordFragment)
        } else
        {   noteViewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_searchFragment_to_beforeAddEditNoteFragment)
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}