package com.example.noteapp.ui.fragments.note

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_note.*


//Sprawdzic notifyDataSetChanged!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

class NoteFragment() : Fragment(), OnItemClickListener{

    private lateinit var viewModel: ViewModel
    private lateinit var noteAdapter: NoteAdapter

    var _binding:FragmentNoteBinding? = null
    val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("addda", "MainFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)


        viewModel.getSortDescNote().observe(requireActivity(), Observer {
            updateNotes(viewModel.allNotes.value!!)
        })

        viewModel.getNoteFabButtonMode().observe(viewLifecycleOwner, Observer {

            updateNotes(viewModel.allNotes.value!!)
        })

        viewModel.getNotifyDataNote().observe(viewLifecycleOwner, Observer {
            if (it==true){
                viewModel.allNotes.value?.forEach { it.isSelected=false }
                updateNotes(viewModel.allNotes.value!!)
                exitMultiSelectMode()
                viewModel.setNotifyDataNote(false)
            }
        })


        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            viewModel.allNotes.value?.forEach { for (i in viewModel.selectedNotes){ if(it.rowId.equals(i.rowId)){
                it.isSelected=true
            }} }

            updateNotes(it)
        })



        checkIsEmpty()
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
        if(viewModel.getMultiSelectMode().value==true){
            if(viewModel.selectedNotes.contains(note)){
                unselectNote(note, position)
            } else{
                selectNote(note, position)
            }
        } else {
            viewModel.setSelectedNote(note)
            viewModel.noteBeforeChange = note
            if(note.hasPassword){
                findNavController().navigate(R.id.action_mainFramgent_to_checkPasswordFragment)
            } else {
                findNavController().navigate(R.id.action_mainFramgent_to_BeforeAddEditNoteFragment)

            }
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        if(viewModel.getMultiSelectMode().value==false){
            viewModel.setMutliSelectMode(true)
            selectNote(note, position)
        }
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

        if(viewModel.selectedNotes.isEmpty()&&viewModel.selectedCategoryItems.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        viewModel.selectedCategoryItems.forEach { it.isSelected = false }
        viewModel.selectedCategoryItems.clear()

        viewModel.selectedNotes.forEach{it.isSelected = false}
        viewModel.selectedNotes.clear()

        viewModel.setMutliSelectMode(false)
    }

    private fun updateNotes(list:List<Note>) {
        Log.d("Abccc", "updateNote")
        val lm =if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        } else{
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        }

        recyclerView.layoutManager = lm

        var listMod:List<Note> = listOf()

        if(viewModel.getNoteFabButtonMode().value==true){
            listMod = list.filter { it.isFavourite == true }
        } else {
            listMod = list
        }

        if(viewModel.getSortDescNote().value!=null) {
            Log.d("sorted", "!=null")
            noteAdapter = if (viewModel.getSortDescNote().value!!) {
                NoteAdapter(listMod, this)

            } else {
                NoteAdapter(listMod.asReversed(), this)
            }
        } else{
            Log.d("sorted", "==null")
            noteAdapter = if (viewModel.p) {
                NoteAdapter(listMod, this)
            } else {
                NoteAdapter(listMod.asReversed(), this)
            }
        }

        recyclerView.adapter = noteAdapter

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