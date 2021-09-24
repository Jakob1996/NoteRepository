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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentSearchNoteBinding
import com.example.noteapp.tools.OnItemClickListener
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.viewmodels.NoteViewModel

class SearchNoteFragment : BaseFragment(), OnItemClickListener {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var noteAdapter: NoteAdapter

    private var _binding: FragmentSearchNoteBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    noteViewModel.search = null
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSoftKeyboard()

        binding.editSearcher.requestFocus()

        noteViewModel.getAllNotes.observe(viewLifecycleOwner, {
            if (noteViewModel.search != null) {
                updateNotes(it, noteViewModel.search!!)
            } else {
                updateNotesEmpty()
            }
        })

        if (noteViewModel.search != null) {
            binding.editSearcher.setText(noteViewModel.search)
            binding.editSearcher.requestFocus(binding.editSearcher.text.length)
        }

        binding.fragmentSearchCategoryToolbarBackIv.setOnClickListener {
            noteViewModel.search = null
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.editSearcher.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (!s.isNullOrEmpty()) {
                    updateNotes(noteViewModel.getAllNotes.value!!, s.toString())
                    noteViewModel.search = binding.editSearcher.text.toString()
                } else updateNotesEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.closeBtn.setOnClickListener {
            if (binding.editSearcher.text.isNotEmpty()) {
                binding.editSearcher.text.clear()
            } else {
                noteViewModel.search = null
                closeKeyboard()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun updateNotes(list: List<Note>, search: String) {
        val lm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerViewInSearch.layoutManager = lm

        val listMod = list.filter {
            it.title.toLowerCase().contains(search.toLowerCase()) || it.message.toUpperCase()
                .contains(search.toUpperCase())
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
        if (note.hasPassword) {
            noteViewModel.isSearchEdit = 2
            findNavController().navigate(R.id.action_search_note_fragment_to_check_password_fragment)
        } else {
            noteViewModel.isSearchEdit = 2
            navigateToFragment(findNavController(), R.id.generalNoteFragment)
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        //NOT YET
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    private fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}