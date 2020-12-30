package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
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

        viewModel.setSelectedNote(null)
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

    fun RecyclerView.smoothSnapToPosition(position: Int, snapMode:Int = LinearSmoothScroller.SNAP_TO_START){
        val smoothScroller = object : LinearSmoothScroller(this.context){
            override fun getVerticalSnapPreference(): Int = snapMode

            override fun getHorizontalSnapPreference(): Int = snapMode
        }

        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("addda", "MainFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)

        //updateModeUI()
        checkIsEmpty()

        /*
        addNote_FB.setOnClickListener {
            if(viewModel.multiSelectMode) {
                viewModel.delete(viewModel.selectedNotes.toList())
                Toast.makeText(requireContext(), "Notes deleted", Toast.LENGTH_LONG).show()
                exitMultiSelectMode()
                checkIsEmpty()
            } else{
                viewModel.setSelectedNote(null)
                findNavController().navigate(R.id.action_mainFramgent_to_addEditNoteFragment)
            }
        }

        sortDate_FB.setOnClickListener {
            val sortDialogFragment = SortDialogFragment()
            sortDialogFragment.setTargetFragment(this, request_code)
            sortDialogFragment.show(parentFragmentManager, "SortDialogFragment")
        }

         */
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("adda", "MainFragment onActivityCreated")

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            updateNotes(it)

            viewModel.allNotes.value?.forEach { for (i in viewModel.selectedNotes){ if(it.rowId.equals(i.rowId)){
                it.isSelected=true
            }} }

            updateNotes(it)
            //updateModeUI()
        })
    }

    private fun checkIsEmpty() {
        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            if(viewModel.allNotes.value!!.size==0){
                empty_textView.hint = "Empty notes"
            } else{
                empty_textView.hint = " "
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
            findNavController().navigate(R.id.action_mainFramgent_to_addEditNoteFragment)
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        if(viewModel.getMultiSelectNote().value==false){
            viewModel.setMutliSelectNote(true)
            selectNote(note, position)
            //updateModeUI()
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
        //updateModeUI()

        noteAdapter.notifyDataSetChanged() // Zmuszamy nasz layout do stworzenia jeszcze raz widoków i odświeży nam
        // wszystkie zaznaczone elementy
    }

    /*
    private fun updateModeUI() {
        if(viewModel.multiSelectMode){
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_baseline_delete))
            addNote_FB.labelText = "Delete notes"
        }else{
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_note_add))
            addNote_FB.labelText = "Add note"
        }
    }

     */

    private fun updateNotes(list:List<Note>) {
        val lm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = lm

        noteAdapter = if(viewModel.sortDescNote) {
            NoteAdapter(list, this)

        } else{
            NoteAdapter(list.asReversed(), this) // asReversed - Na odwrót
        }

        recyclerView.adapter = noteAdapter

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        //recyclerView.smoothScrollToPosition(noteAdapter.itemCount)

        checkIsEmpty()
        noteAdapter.notifyDataSetChanged()
    }
}