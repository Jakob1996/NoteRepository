package com.example.noteapp.ui.fragments.note

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentNotesListBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.tools.OnItemClickListener
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.TodoViewModel
import fadeIn
import fadeOut

class NoteListFragment() : Fragment(), OnItemClickListener, Navigation {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var todoViewModel: TodoViewModel

    private lateinit var noteAdapter: NoteAdapter

    private var _binding: FragmentNotesListBinding? = null

    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)

        noteViewModel.getSortDescNote().observe(requireActivity(), {
            updateNotes(noteViewModel.getAllNotes.value!!)
        })

        noteViewModel.getNoteFabButtonMode().observe(viewLifecycleOwner, {
            updateNotes(noteViewModel.getAllNotes.value!!)
            if (noteViewModel.getAllNotes.value!!.none { it.isFavourite }
                && noteViewModel.getNoteFabButtonMode().value == true) {
                binding.fragmentNotesListEmptyFavouritiesTv.fadeIn()
            } else {
                binding.fragmentNotesListEmptyFavouritiesTv.fadeOut()
            }
        })

        noteViewModel.getNotifyDataNote().observe(viewLifecycleOwner, {
            if (it == true) {
                noteViewModel.getAllNotes.value?.forEach { it.isSelected = false }
                updateNotes(noteViewModel.getAllNotes.value!!)
                exitMultiSelectMode()
                noteViewModel.setNotifyDataNote(false)
            }
        })

        noteViewModel.getAllNotes.observe(viewLifecycleOwner, { it ->
            noteViewModel.getAllNotes.value?.forEach {
                for (i in noteViewModel.selectedNotes) {
                    if (it.rowId == i.rowId) {
                        it.isSelected = true
                    }
                }
            }
            updateNotes(it)
        })
        checkIsEmpty()

        return binding.root
    }

    private fun checkIsEmpty() {
        noteViewModel.getAllNotes.observe(viewLifecycleOwner, {
            if (noteViewModel.getAllNotes.value!!.isEmpty()) {
                binding.fragmentNotesListEmptyTv.fadeIn()
            } else {
                binding.fragmentNotesListEmptyTv.fadeOut()
            }
        })
    }

    override fun onItemClick(note: Note, position: Int) {
        if (noteViewModel.getMultiSelectMode().value == true) {
            if (noteViewModel.selectedNotes.contains(note)) {
                unselectNote(note, position)
            } else {
                selectNote(note, position)
            }
        } else {
            noteViewModel.noteBeforeChange = note
            noteViewModel.setSelectedNote(note)
            noteViewModel.titleBefore = note.title
            noteViewModel.messageBefore = note.message
            if (note.hasPassword) {
                navigateToFragment(
                    findNavController(), R.id.action_to_check_password_fragment
                )
            } else {
                navigateToFragment(
                    findNavController(), R.id.action_to_general_note_fragment
                )
            }
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        if (noteViewModel.getMultiSelectMode().value == false) {
            noteViewModel.setMutliSelectMode(true)
            selectNote(note, position)
        }
    }

    private fun selectNote(note: Note, position: Int) {
        note.isSelected = true
        noteViewModel.selectedNotes.add(note)
        noteAdapter.notifyItemChanged(position)
    }

    private fun unselectNote(note: Note, position: Int) {
        noteViewModel.selectedNotes.remove(note)

        note.isSelected = false

        noteAdapter.notifyItemChanged(position)

        if (noteViewModel.selectedNotes.isEmpty() && todoViewModel.selectedCategoryItems.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        todoViewModel.selectedCategoryItems.forEach { it.isSelected = false }
        todoViewModel.selectedCategoryItems.clear()

        noteViewModel.selectedNotes.forEach { it.isSelected = false }
        noteViewModel.selectedNotes.clear()

        noteViewModel.setMutliSelectMode(false)
    }

    private fun updateNotes(list: List<Note>) {

        val lm = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        } else {
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.fragmentNotesListRv.layoutManager = lm

        val listMod: List<Note>

        if (noteViewModel.getNoteFabButtonMode().value == true) {
            listMod = list.filter { it.isFavourite }
        } else {
            listMod = list
        }

        if (noteViewModel.getSortDescNote().value != null) {
            noteAdapter = if (noteViewModel.getSortDescNote().value!!) {
                NoteAdapter(listMod, this)
            } else {
                NoteAdapter(listMod.asReversed(), this)
            }
        } else {
            noteAdapter = if (noteViewModel.state) {
                NoteAdapter(listMod, this)
            } else {
                NoteAdapter(listMod.asReversed(), this)
            }
        }

        binding.fragmentNotesListRv.adapter = noteAdapter

        if (noteViewModel.noteState != null) {
            (binding.fragmentNotesListRv.layoutManager as StaggeredGridLayoutManager).onRestoreInstanceState(
                noteViewModel.noteState
            )
        }

        checkIsEmpty()
        noteAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()

        noteViewModel.noteState = binding.fragmentNotesListRv.layoutManager?.onSaveInstanceState()
    }
}