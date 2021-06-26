package com.example.noteapp.ui.fragments.note

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.tools.Clic
import com.example.noteapp.tools.OnItemClickListener
import com.example.noteapp.ui.fragments.main.ClBk
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ToDoViewModel
import kotlinx.coroutines.*

class NoteFragment() : Fragment(), OnItemClickListener, Navigation, ClBk {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: ToDoViewModel
    private lateinit var noteAdapter: NoteAdapter

    private var _binding:FragmentNoteBinding? = null
    private val  binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container , false)


        noteViewModel.getSortDescNote().observe(requireActivity(), Observer {
            updateNotes(noteViewModel.allNotes.value!!)
        })

        noteViewModel.getNoteFabButtonMode().observe(viewLifecycleOwner, Observer {
            updateNotes(noteViewModel.allNotes.value!!)
        })

        noteViewModel.getNotifyDataNote().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                noteViewModel.allNotes.value?.forEach { it.isSelected = false }
                updateNotes(noteViewModel.allNotes.value!!)
                exitMultiSelectMode()
                noteViewModel.setNotifyDataNote(false)
            }
        })

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer { it ->
            noteViewModel.allNotes.value?.forEach {
                for (i in noteViewModel.selectedNotes) {
                    if (it.rowId == i.rowId) {
                        it.isSelected = true
                    }
                }
            }
            updateNotes(it)
        })
        checkIsEmpty()

        Log.d("123", "1")
        GlobalScope.launch(Dispatchers.IO){
            delay(5000)

        }

        return binding.root
    }

    private fun checkIsEmpty() {
        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            if (noteViewModel.allNotes.value!!.isEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
            } else {
                binding.emptyTextView.visibility = View.GONE
            }
        })
    }

    override fun onItemClick(note: Note, position: Int) {
        if(noteViewModel.getMultiSelectMode().value==true){
            if(noteViewModel.selectedNotes.contains(note)){
                unselectNote(note, position)
            } else {
                selectNote(note, position)
            }
        } else {
            noteViewModel.noteBeforeChange = note
            noteViewModel.setSelectedNote(note)
            noteViewModel.titleBefore = note.title
            noteViewModel.messageBefore = note.message
            if(note.hasPassword){
                /*
                val fragManager = requireActivity().supportFragmentManager
                val frag = CheckPasswordFragment()
                val transaction = fragManager.beginTransaction()
                        .setCustomAnimations(R.anim.from_right_to_left, R.anim.zero_to_zero, R.anim.zero_to_zero, R.anim.from_left_to_right)
                transaction.add(R.id.container_keeper, frag, "noteF").addToBackStack("noteF")
                transaction.commit()
                 */
                     showDialog(CheckPasswordDialogFragment(),"Abc", requireActivity().supportFragmentManager, 1111)
            } else {
                navigateToFragment(BeforeEditNoteFragment(), "noteF", requireActivity().supportFragmentManager)
            }
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        if(noteViewModel.getMultiSelectMode().value==false){
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

        if(noteViewModel.selectedNotes.isEmpty()&&todoViewModel.selectedCategoryItems.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        todoViewModel.selectedCategoryItems.forEach { it.isSelected = false }
        todoViewModel.selectedCategoryItems.clear()

        noteViewModel.selectedNotes.forEach{it.isSelected = false}
        noteViewModel.selectedNotes.clear()

        noteViewModel.setMutliSelectMode(false)
    }

    private fun updateNotes(list: List<Note>) {

        val lm =if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        } else{
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        }

        binding.recyclerView.layoutManager = lm

        val listMod:List<Note>

        if(noteViewModel.getNoteFabButtonMode().value==true){
            listMod = list.filter { it.isFavourite == true }
        } else {
            listMod = list
        }

        if(noteViewModel.getSortDescNote().value!=null) {
            noteAdapter = if (noteViewModel.getSortDescNote().value!!) {
                NoteAdapter(listMod, this)

            } else {
                NoteAdapter(listMod.asReversed(), this)
            }
        } else{
            noteAdapter = if (noteViewModel.p) {
                NoteAdapter(listMod, this)
            } else {
                NoteAdapter(listMod.asReversed(), this)
            }
        }

        binding.recyclerView.adapter = noteAdapter

        if(noteViewModel.noteState!=null){
            (binding.recyclerView.layoutManager as StaggeredGridLayoutManager).onRestoreInstanceState(noteViewModel.noteState)
        }

        checkIsEmpty()
        noteAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()

        noteViewModel.noteState = binding.recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun callBack() {
        binding.emptyTextView.visibility = View.VISIBLE
    }


}