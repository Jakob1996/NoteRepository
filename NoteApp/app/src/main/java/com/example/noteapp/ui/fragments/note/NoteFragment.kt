package com.example.noteapp.ui.fragments.note

import android.content.res.Configuration
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.data.Note
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_note.*

class NoteFragment() : Fragment(), OnItemClickListener, SortDialogFragment.OnItemClickDialogListener {

    private lateinit var viewModel: ViewModel
    private lateinit var noteAdapter: NoteAdapter
    private val request_code = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("addda", "MainFragment onCreate")
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(viewModel.getMultiSelectNote().value == true){
                    exitMultiSelectMode()
                } else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("addda", "MainFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        checkIsEmpty()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("adda", "MainFragment onActivityCreated")
        viewModel.setSelectedNote(null)

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {

            viewModel.allNotes.value?.forEach { for (i in viewModel.selectedNotes){ if(it.rowId.equals(i.rowId)){
                it.isSelected=true
            }} }

            updateNotes(it)
        })
    }

    private fun checkIsEmpty() {
        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            if(viewModel.allNotes.value!!.size==0){
                empty_textView.visibility = View.VISIBLE
            } else{
                empty_textView.visibility = View.GONE
            }
        })
    }

    override fun onItemClick(note: Note, position: Int) {
        if(viewModel.getMultiSelectNote().value==true){
            if(viewModel.selectedNotes.contains(note)){
                unselectNote(note, position)
            } else{
                selectNote(note, position)
            }
        } else {
            viewModel.setSelectedNote(note)
            viewModel.setSelectedNoteBeforeChange(note)
            if(note.hasPassword){
                findNavController().navigate(R.id.action_mainFramgent_to_checkPasswordFragment)
            } else
            {
                findNavController().navigate(R.id.action_mainFramgent_to_addEditNoteFragment)
            }
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        if(viewModel.getMultiSelectNote().value==false){
            viewModel.setMutliSelectNote(true)
            selectNote(note, position)
        }
    }

    override fun onItemClickDialog(sortDesc: Boolean) {
        viewModel.sortDescNote = sortDesc
        updateNotes(viewModel.allNotes.value!!)
    }

    private fun selectNote(note: Note, position: Int) {
        note.isSelected = true
        viewModel.selectedNotes.add(note)
        noteAdapter.notifyItemChanged(position)
    }

    private fun unselectNote(note: Note, position: Int) {
        viewModel.selectedNotes.remove(note)

        note.isSelected = false

        noteAdapter.notifyItemChanged(position)

        if(viewModel.selectedNotes.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        viewModel.setMutliSelectNote(false)
        viewModel.selectedNotes.forEach{it.isSelected = false}
        viewModel.selectedNotes.clear()

        noteAdapter.notifyDataSetChanged() // Zmuszamy nasz layout do stworzenia jeszcze raz widoków i odświeży nam
        // wszystkie zaznaczone elementy
    }

    private fun updateNotes(list:List<Note>) {

        val lm =if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        } else{
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        }


        recyclerView.layoutManager = lm

        noteAdapter = if(viewModel.sortDescNote) {
            NoteAdapter(list, this, requireContext())

        } else{
            NoteAdapter(list.asReversed(), this, requireContext()) // asReversed - Na odwrót
        }

        recyclerView.adapter = noteAdapter
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        if(viewModel.noteState!=null){
            (recyclerView.layoutManager as StaggeredGridLayoutManager).onRestoreInstanceState(viewModel.noteState)
        }

        checkIsEmpty()
        noteAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()

        viewModel.noteState = recyclerView.layoutManager?.onSaveInstanceState()
    }
}