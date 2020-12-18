package com.example.noteapp.ui.fragments


import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_note.*

class NoteFragment : Fragment(), OnItemClickListener, SortDialogFragment.OnItemClickDialogListener {

    private lateinit var viewModel: NotesViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var searchView: SearchView
    private val request_code = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("addda", "MainFragment onCreate")
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true) // Nasz fragment dostaje dostęp do naszego paska menu

        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        viewModel.setSelectedNote(null)
        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(viewModel.multiSelectMode ){
                    exitMultiSelectMode()
                } else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("addda", "MainFragment onCreateView")
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("addda", "MainFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)

        updateModeUI()
        checkIsEmpty()


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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("adda", "MainFragment onActivityCreated")

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            Log.d("ppas", "${viewModel.multiSelectMode}")
            Log.d("ppas", "${viewModel.selectedNotes.size}")

            updateNotes(it)

            viewModel.allNotes.value?.forEach { for (i in viewModel.selectedNotes){ if(it.rowId.equals(i.rowId)){
                it.isSelected=true
            }} }

            updateNotes(it)
            updateModeUI()

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("addda", "MainFragment onCreateOptionsMenu")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu) // Co rozdmuchać i w czym

        val menuItem = menu.findItem(R.id.action_search)
        searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search in notes..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { // Metoda ktora sie wykonuje dopiero po zatwierdzeniu
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean { // Metoda ktora sie wykonuje po kazdej zmianie tekstu
                updateNotes(viewModel.findInNotes(newText.toString()))
                return false
            }
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
        if(viewModel.multiSelectMode){
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

        Log.d("koty", "Wybierana ${note.title}, ${note.message}, ${note.isSelected}, ${note.color}, ${note.date}, ${note.rowId}")

        for(i in viewModel.selectedNotes){
            Log.d("koty", "${i.toString()}")
        }

        viewModel.selectedNotes.remove(note)

        note.isSelected = false
        /*
        Log.d("koty", "${viewModel.selectedNotes.size}")
        for(i in  0..viewModel.selectedNotes.size-1){
            if(viewModel.selectedNotes[i].rowId==note.rowId){
                viewModel.selectedNotes.remove(viewModel.selectedNotes[i])
            }
        }
         */


        noteAdapter.notifyItemChanged(position)

        Log.d("addda", "unselect")

        if(viewModel.selectedNotes.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        Log.d("addda", "exit")
        viewModel.multiSelectMode = false
        viewModel.selectedNotes.forEach{it.isSelected = false}
        viewModel.selectedNotes.clear()
        updateModeUI()

        noteAdapter.notifyDataSetChanged() // Zmuszamy nasz layout do stworzenia jeszcze raz widoków i odświeży nam
        // wszystkie zaznaczone elementy
    }

    private fun updateModeUI() {
        Log.d("addda", "update")
        if(viewModel.multiSelectMode){
            (requireActivity() as AppCompatActivity).supportActionBar?.title ="Multi-select-mode"
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_baseline_delete))
            addNote_FB.labelText = "Delete notes"
        }else{
            (requireActivity() as AppCompatActivity).supportActionBar?.title ="All notes"
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_note_add))
            addNote_FB.labelText = "Add note"
        }
    }

    private fun updateNotes(list:List<Note>) {
        val lm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = lm


        noteAdapter = if(viewModel.sortDesc) {
            NoteAdapter(list, this)
        } else{
            NoteAdapter(list.asReversed(), this) // asReversed - Na odwrót
        }

        recyclerView.adapter = noteAdapter

        checkIsEmpty()
        noteAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            if(!searchView.isIconified&&searchView.isNotEmpty()){
                searchView.isIconified = true
                searchView.isIconified = true
            } else if(!searchView.isIconified){
                searchView.isIconified = true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}