package com.example.noteapp.ui.fragments

import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_main_framgent.*


class MainFragment : Fragment(), OnItemClickListener, SortDialogFragment.OnItemClickDialogListener {

    private lateinit var viewModel: NotesViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var searchView: SearchView
    private val request_code = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Nasz fragment dostaje dostęp do naszego paska menu
        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(!searchView.isIconified){ //Jesli nasz searchView nie jest ikoną
                    searchView.onActionViewCollapsed()
                    updateModeUI()
                }else{
                    if(viewModel.multiSelectMode ){
                        exitMultiSelectMode()
                    } else{
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu) // Co rozdmuchać i w czym
        val menuItem = menu.findItem(R.id.action_search)
        searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search in notes"

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { // Metoda ktora sie wykonuje dopiero po zatwierdzeniu
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean { // Metoda ktora sie wykonuje po kazdej zmianie tekstu
                updateNotes(viewModel.findInNotes(newText.toString()))
                return false
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_framgent, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.allNotes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateNotes(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("log", "onViewCreated in MainFragment")
        recyclerView.layoutManager =
                if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                    GridLayoutManager(requireContext(), 2)
        } else {
            GridLayoutManager(requireContext(), 1)
        }


        addNote_FB.setOnClickListener {
            if(viewModel.multiSelectMode){
                viewModel.delete(viewModel.selectedNotes.toList())
                exitMultiSelectMode()
            }else {

                viewModel.setSelectedNote(null)
                findNavController().navigate(R.id.addEditNoteFragment)
            }
        }

        sortDate_FB.setOnClickListener {
            val sortDialogFragment = SortDialogFragment()
            sortDialogFragment.setTargetFragment(this, request_code)
            sortDialogFragment.show(parentFragmentManager, "SortDialogFragment")
        }

        updateModeUI()
    }

    private fun updateNotes(list:List<Note>) {
        noteAdapter = if(viewModel.sortDesc) {
            NoteAdapter(list, this)
        } else{
            NoteAdapter(list.asReversed(), this) // asReversed - Na odwrót
        }

        recyclerView.adapter = noteAdapter

    }

    override fun onItemClick(note: Note, position: Int) {
        if(viewModel.multiSelectMode){
            if(viewModel.selectedNotes.contains(note)){
                unselectNote(note, position)
            } else{
                selectNote(note, position)
            }
        } else {
            viewModel.setSelectedNote(note)
            findNavController().navigate(R.id.addEditNoteFragment)
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        if(!viewModel.multiSelectMode){
            viewModel.multiSelectMode = !viewModel.multiSelectMode
            selectNote(note, position)
            updateModeUI()
        }
    }

    override fun onItemClickDialog(sortDesc: Boolean) {
        viewModel.sortDesc = sortDesc
        updateNotes(viewModel.allNotes.value!!)
    }

    private fun selectNote(note: Note, position: Int) {
        note.isSelected = true
        viewModel.selectedNotes.add(note)
        noteAdapter.notifyItemChanged(position)
    }

    private fun unselectNote(note: Note, position: Int) {
        note.isSelected = false
        viewModel.selectedNotes.remove(note)
        noteAdapter.notifyItemChanged(position)

        if(viewModel.selectedNotes.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        viewModel.multiSelectMode = false
        viewModel.selectedNotes.forEach{it.isSelected = false}
        viewModel.selectedNotes.clear()
        updateModeUI()
        noteAdapter.notifyDataSetChanged() // Zmuszamy nasz layout do stworzenia jeszcze raz widoków i odświeży nam
                                            // wszystkie zaznaczone elementy
    }

    private fun updateModeUI() {
        if(viewModel.multiSelectMode){
            (requireActivity() as AppCompatActivity).supportActionBar?.title ="Multi-select-mode"
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_baseline_delete))
            addNote_FB.labelText = "Delete notes"
        }else{
            (requireActivity() as AppCompatActivity).supportActionBar?.title ="Your notes"
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_note_add))
            addNote_FB.labelText = "Add note"

        }
    }
}
