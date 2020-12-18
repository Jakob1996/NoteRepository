package com.example.noteapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import kotlinx.android.synthetic.main.layout_miscellaneous.*
import java.lang.Exception
import java.util.*

class AddEditNoteFragment:Fragment() {

    private lateinit var viewModel: NotesViewModel

    private var value:Boolean=false

    private var selectedImagePath:String = ""

    private val REQUEST_CODE_STORAGE_PERMISSION = 1

    private val REQUEST_CODE_SELECT_IMAGE = 2
    private val RESULT_OK = -1

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("xTa", "AddEditNoteFragment onCreate")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        setHasOptionsMenu(true)

            requireActivity().onBackPressedDispatcher.addCallback(
                    this,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            Log.d("tits", "kot")
                            if (value==true) {
                               isEnabled=false
                                requireActivity().onBackPressed()
                            } else{
                            //Po naciśnięciu przycisku wstecz sprawdzamy czy notatka nie jest pusta
                                if (title_addEditFrag.text.isNotEmpty() || mess_addEditFrag.text.isNotEmpty()) {
                                    Log.d("asdfg", "$selectedImagePath")
                                    val title = title_addEditFrag.text.toString()
                                    val message = mess_addEditFrag.text.toString()
                                    val date = Calendar.getInstance().timeInMillis
                                    val color= viewModel.selectedNoteColor
                                    Log.d("tits", color)

                                    //Jeśli notatka nie jest pusta, oraz nie jest zaznaczona w MainFragment - Tworzymy
                                    if (viewModel.getSelectedNote().value == null) {
                                        val note = Note(title, message, date, isSelected = false, color, selectedImagePath)
                                        viewModel.insert(note)
                                        Log.d("kot", "1")
                                        Toast.makeText(
                                            requireContext(),
                                            "Note created",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()

                                        //Jeśli notatka nie jest pusta, ale jest zaznaczona w MainFragment - Aktualizujemy
                                    } else {
                                        val selectedNote = viewModel.getSelectedNote().value!!
                                        if (selectedNote.title != title || selectedNote.message != message || selectedNote.color != color) {
                                            val note = Note(title, message, date, isSelected = false, color, selectedImagePath).apply {
                                                rowId = viewModel.getSelectedNote().value!!.rowId
                                            }
                                            Toast.makeText(
                                                requireContext(),
                                                "Note updated",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            viewModel.update(note)
                                        }
                                    }
                                } else if (viewModel.getSelectedNote().value != null) {
                                    viewModel.deleteOneNote(viewModel.getSelectedNote().value)
                                    Toast.makeText(
                                        requireContext(),
                                        "Note deleted",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }

                            /*else {
                        Log.d("adds", "ff")
                        viewModel.deleteOneNote(viewModel.getSelectedNote().value)
                    }
                     */
                            viewModel.setSelectedNote(null)
                            isEnabled = false
                            closeKeyboard()
                                findNavController().navigate(R.id.action_addEditNoteFragment_to_mainFramgent2)
                        }
                        }
                    })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d("xTa", "AddEditNoteFragment onCreateView")
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("tits", "AddEditNoteFragment onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val gradientDrawable:GradientDrawable = viewSubtitleIndicator.background as GradientDrawable

        Log.d("tits", "1")

        initMiscellaneous()

        back_from_addEdit.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_addEditNoteFragment_to_mainFramgent2)
            }
        })

        done.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (title_addEditFrag.text.isNotEmpty() || mess_addEditFrag.text.isNotEmpty()) {
                    Log.d("asdfg", "$selectedImagePath")
                    val title = title_addEditFrag.text.toString()
                    val message = mess_addEditFrag.text.toString()
                    val date = Calendar.getInstance().timeInMillis
                    val color= viewModel.selectedNoteColor
                    Log.d("tits", color)

                    //Jeśli notatka nie jest pusta, oraz nie jest zaznaczona w MainFragment - Tworzymy
                    if (viewModel.getSelectedNote().value == null) {
                        val note = Note(title, message, date, isSelected = false, color, selectedImagePath)
                        viewModel.insert(note)
                        Log.d("kot", "1")
                        Toast.makeText(
                                requireContext(),
                                "Note created",
                                Toast.LENGTH_LONG
                        )
                                .show()

                        //Jeśli notatka nie jest pusta, ale jest zaznaczona w MainFragment - Aktualizujemy
                    } else {
                        val selectedNote = viewModel.getSelectedNote().value!!
                        if (selectedNote.title != title || selectedNote.message != message || selectedNote.color != color) {
                            val note = Note(title, message, date, isSelected = false, color, selectedImagePath).apply {
                                rowId = viewModel.getSelectedNote().value!!.rowId
                            }
                            Toast.makeText(
                                    requireContext(),
                                    "Note updated",
                                    Toast.LENGTH_LONG
                            ).show()
                            viewModel.update(note)
                        }
                    }
                } else if (viewModel.getSelectedNote().value != null) {
                    viewModel.deleteOneNote(viewModel.getSelectedNote().value)
                    Toast.makeText(
                            requireContext(),
                            "Note deleted",
                            Toast.LENGTH_LONG
                    )
                            .show()
                }

                /*else {
            Log.d("adds", "ff")
            viewModel.deleteOneNote(viewModel.getSelectedNote().value)
        }
         */
                viewModel.setSelectedNote(null)
                closeKeyboard()
                findNavController().navigate(R.id.action_addEditNoteFragment_to_mainFramgent2)
            }
        })

        imageView = requireActivity().findViewById(R.id.imageNote)

            viewModel.getSelectedNote().observe(viewLifecycleOwner, Observer { note ->
                note.let {
                        title_addEditFrag.setText(it?.title)
                        mess_addEditFrag.setText(it?.message)

                    if((title_addEditFrag.text.isEmpty()&&mess_addEditFrag.text.isEmpty())) {
                        viewModel.selectedNoteColor = "#333333"
                        gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))

                    } else {
                        toolbar_Title_AddEdit.text = "Note editor"
                        gradientDrawable.setColor(Color.parseColor(viewModel.getSelectedNote().value?.color))
                        viewModel.selectedNoteColor = viewModel.getSelectedNote().value!!.color

                        if(!note!!.imagePath.isEmpty()){
                            selectedImagePath = note.imagePath
                            imageView.setImageBitmap(BitmapFactory.decodeFile(note!!.imagePath))
                            imageView.visibility = View.VISIBLE
                        }

                        when(viewModel.getSelectedNote().value?.color){
                            "#333333"-> {
                                itemSelected1()
                            }

                            "#FDBE3B" -> {
                                itemSelected2()
                            }

                            "#FF4842" -> {
                                itemSelected3()
                            }

                            "#ff0266" ->{
                                itemSelected6()
                            }

                            "#3A52FC" -> {
                                itemSelected4()
                            }

                            "#000000" -> {
                                itemSelected5()
                            }
                        }
                    }
                }
            })
    }

    fun closeKeyboard(){
        val view = requireActivity().currentFocus
        if(view!=null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("xTa", "MainFragment onCreateOptionsMenu")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_edit_item, menu) // Co rozdmuchać i w czym

    }

    fun setSubtitleIndicator(){
        val gradientDrawable:GradientDrawable = viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))
    }

    fun selectImage(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if(intent.resolveActivity(requireActivity().packageManager) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.size>0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage()
            } else{
                Toast.makeText(requireContext(), "Permission Denited", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun initMiscellaneous(){
        val layoutMiscellaneous = requireActivity().findViewById<LinearLayout>(R.id.layoutMiscellaneous)
        val bottomSheetBehavior = BottomSheetBehavior.from(requireActivity().findViewById(R.id.layoutMiscellaneous))

        layoutMiscellaneous.findViewById<TextView>(R.id.textMiscellaneous).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                if(bottomSheetBehavior.state!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else{
                    bottomSheetBehavior.state =BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor1).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#333333"
                itemSelected1()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor2).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FDBE3B"
                itemSelected2()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor3).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FF4842"
                itemSelected3()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor4).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#3A52FC"
                itemSelected4()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor5).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#000000"
               itemSelected5()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor6).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#ff0266"
                itemSelected6()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<LinearLayout>(R.id.layoutAddImage).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
                } else{
                    selectImage()
                }
            }
        })
    }

    fun itemSelected1(){
        imageColor1.setImageResource(R.drawable.ic_done)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected2(){
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(R.drawable.ic_done)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected3(){
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(R.drawable.ic_done)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected6(){
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(R.drawable.ic_done)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }


    fun itemSelected4(){
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(R.drawable.ic_done)
        imageColor5.setImageResource(0)
    }

    fun itemSelected5(){
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(R.drawable.ic_done)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data!=null){
                val selectedImageUri:Uri? = data.data
                if(selectedImageUri != null){
                    try {
                        val inputStream = requireActivity().contentResolver.openInputStream(selectedImageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        imageNote.setImageBitmap(bitmap)
                        imageNote.visibility = View.VISIBLE
                        selectedImagePath = getPathFromUri(selectedImageUri)

                    } catch (exeption:Exception){
                        Toast.makeText(context, "${exeption.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        Log.d("kottt", "aaa $selectedImagePath")
    }

    fun getPathFromUri(contentUri:Uri): String {
        val filePath:String
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if(cursor == null){
            filePath = contentUri.path!!
        } else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        
        return filePath
    }
}