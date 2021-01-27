package com.example.noteapp.ui.fragments.note

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.ImageAdapter
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.fragment_before_edit_note.*
import kotlinx.android.synthetic.main.note_edit_layout_miscellaneous.*
import java.util.*
import kotlin.collections.ArrayList

class BeforeEditNoteFragment:Fragment() {

    private lateinit var imageAdapter:ImageAdapter

    private lateinit var viewModel: ViewModel

    private var value: Boolean = false

    private var selectedImagePath: String = ""

    private val REQUEST_CODE_STORAGE_PERMISSION = 1

    private val REQUEST_CODE_SELECT_IMAGE = 2

    private val RESULT_OK = -1

    private var quit = 1

    private var imageList:ArrayList<Uri>? = null

    private var imageListString:ArrayList<String>? = arrayListOf()

    //private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("xTa", "AddEditNoteFragment onCreate")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val title = title_addEditFrag.text.toString()
                        val message = mess_addEditFrag.text.toString()
                        val date = Calendar.getInstance().timeInMillis
                        val color = viewModel.selectedNoteColor
                        val fontSize = viewModel.selectedFontSize
                        val path = viewModel.pathImage
                        val fontColor = viewModel.selectedFontNote
                        val favourite = viewModel.isFavourite
                        val hasPassword = viewModel.hasPassword
                        val password  = viewModel.password



                        val noteBefore = viewModel.getSelectedNoteBeforeChange().value!!

                        if (noteBefore.title != title || noteBefore.message != message || noteBefore.color != color
                                || fontSize!= noteBefore.fontSize ||  fontColor != noteBefore.fontColor || noteBefore.imagePaths != path
                                || noteBefore.title.length!=title.length || noteBefore.message.length != message.length || noteBefore.isFavourite!= favourite||noteBefore.hasPassword!=hasPassword) {
                            val note = Note(
                                    title,
                                    message,
                                    date,
                                    false,
                                    viewModel.selectedNoteColor,
                                   path,
                                    viewModel.selectedFontNote,
                                    viewModel.selectedFontSize,
                                    viewModel.isFavourite,
                                    viewModel.hasPassword,
                                    viewModel.password
                            ).apply {
                                rowId = viewModel.getSelectedNote().value!!.rowId
                            }
                            viewModel.updateNote(note)
                            viewModel.noteState = null
                        }
                        quit = 2
                        isEnabled = false
                        closeKeyboard()
                        findNavController().navigate(R.id.action_addEditNoteFragment_to_mainFramgent2)
                    }
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_before_edit_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gradientDrawable: GradientDrawable = viewSubtitleIndicator.background as GradientDrawable

        initMiscellaneous()

        layoutPasswordImage.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.hasPassword){
                    val fm = requireActivity().supportFragmentManager
                    val dialogFrag = RemovePasswordDialogFragment()
                    dialogFrag.show(fm, "Ab")
                } else{
                    findNavController().navigate(R.id.action_addEditNoteFragment_to_passwordNoteFragment)
                }
            }
        })

        editNote.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_before_EditNoteFragment_to_editNoteFragment)
            }
        })

        fontSizeL.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ++viewModel.selectedFontSize
                if (viewModel.selectedFontSize > 5) {
                    viewModel.selectedFontSize = 1
                }
                setFontSize(viewModel.selectedFontSize)
                viewModel.selectedFontSize + 1

                if (viewModel.selectedFontSize > 5) {
                    viewModel.selectedFontSize = 1
                }
            }
        })

        colorFontL.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ++viewModel.selectedFontNote
                if (viewModel.selectedFontNote > 3) {
                    viewModel.selectedFontNote = 1
                }
                setFontColor(viewModel.selectedFontNote)
                viewModel.selectedFontNote + 1

                if (viewModel.selectedFontNote > 3) {
                    viewModel.selectedFontNote = 1
                }
            }
        })

        back_from_addEdit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                requireActivity().onBackPressed()
            }
        })

        favouriteNoteButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.isFavourite){
                    viewModel.isFavourite = false
                    setFavourite(viewModel.isFavourite)
                } else{
                    viewModel.isFavourite = true
                    setFavourite(viewModel.isFavourite)
                }
            }
        })

        viewModel.getSelectedNote().observe(viewLifecycleOwner, Observer {

            if(viewModel.getSelectedNote().value==null) {
                viewModel.selectedNoteColor = "#333333"
                gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))
            } else{
                viewModel.noteTitle = it!!.title
                viewModel.noteMessage = it.message
                viewModel.noteDate = it.date
                viewModel.pathImage = it.imagePaths
                viewModel.selectedNoteColor = it.color
                viewModel.selectedFontSize = it.fontSize
                viewModel.selectedFontNote = it.fontColor
                viewModel.idNote = it.rowId
                viewModel.isFavourite = it.isFavourite
                viewModel.hasPassword = it.hasPassword
                viewModel.password = it.password

                Log.d("abbb", "${viewModel.password} hasPassword: ${viewModel.hasPassword}")

                initAdapter(viewModel.pathImage)
                setImagePassword(viewModel.hasPassword)
                setFavourite(viewModel.isFavourite)
                title_addEditFrag.setText(viewModel.noteTitle)
                mess_addEditFrag.setText(viewModel.noteMessage)
                setFontColor(viewModel.selectedFontNote)
                setFontSize(viewModel.selectedFontSize)
                //Glide.with(requireContext()).load(it.imagePath).override(1000, 1000).fitCenter().centerCrop().into(imageView)

                gradientDrawable.setColor(Color.parseColor(viewModel.getSelectedNote().value?.color))
                viewModel.selectedNoteColor = viewModel.getSelectedNote().value!!.color

                when (viewModel.getSelectedNote().value?.color) {
                    "#333333" -> {
                        itemSelected1()
                    }

                    "#FDBE3B" -> {
                        itemSelected2()
                    }

                    "#FF4842" -> {
                        itemSelected3()
                    }

                    "#ff0266" -> {
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
        })
    }

    private fun initAdapter(listImages:ArrayList<String>) {

        val lm = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)

        recyclerViewImageBeforeFrag.layoutManager = lm

        imageAdapter = ImageAdapter(listImages, requireContext())

        recyclerViewImageBeforeFrag.adapter = imageAdapter

        imageAdapter.notifyDataSetChanged()
    }


    fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun setSubtitleIndicator() {
        val gradientDrawable: GradientDrawable = viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))
    }

    fun selectImage() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setType("image/*")
        startActivityForResult(intent, 1)



        /*
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
        }
         */
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Toast.makeText(requireContext(), "Permission Denited", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun initMiscellaneous() {
        val layoutMiscellaneous = requireActivity().findViewById<LinearLayout>(R.id.layoutMiscellaneous)
        val bottomSheetBehavior = BottomSheetBehavior.from(requireActivity().findViewById(R.id.layoutMiscellaneous))

        layoutMiscellaneous.findViewById<TextView>(R.id.textMiscellaneous).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor1).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#333333"
                itemSelected1()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor2).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FDBE3B"
                itemSelected2()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor3).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FF4842"
                itemSelected3()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor4).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#3A52FC"
                itemSelected4()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor5).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#000000"
                itemSelected5()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor6).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#ff0266"
                itemSelected6()
                setSubtitleIndicator()
            }
        })



        layoutAddImage.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                openImagePicker()


                /*
                 if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                 }

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                } else {
                    selectImage()
                }
                 */
            }
        })
    }

    private fun openImagePicker() {
        FishBun.with(this).setImageAdapter(GlideAdapter())
                .setMinCount(1)
                .setMaxCount(10)
                .setAllViewTitle("All")
                .setActionBarTitle("Selected Images")
                .startAlbum()
    }

    fun itemSelected1() {
        imageColor1.setImageResource(R.drawable.ic_done)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected2() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(R.drawable.ic_done)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected3() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(R.drawable.ic_done)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected6() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(R.drawable.ic_done)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected4() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(R.drawable.ic_done)
        imageColor5.setImageResource(0)
    }

    fun itemSelected5() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(R.drawable.ic_done)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            FishBun.FISHBUN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    imageList = data?.getParcelableArrayListExtra(FishBun.INTENT_PATH)


                    imageList?.forEach {
                        viewModel.pathImage.add(it.toString())
                    }

                    initAdapter(viewModel.pathImage)
                }
            }
        }

        /*
        if(requestCode == 1 && requestCode == RESULT_OK){
            val listBitmaps = arrayListOf<Bitmap>()
            val clipData = data?.clipData
            if(clipData!= null){
              for (i in 0..clipData.itemCount){
                  val imageUri = clipData.getItemAt(i).uri
                  try {
                      val inpStr = context?.contentResolver?.openInputStream(imageUri)
                      val bitm = BitmapFactory.decodeStream(inpStr)
                      listBitmaps.add(bitm)
                  } catch ( e : FileNotFoundException){
                      e.printStackTrace()
                  }
              }
            } else{
                val imageUri = data?.data
                try {
                    val inpStr = context?.contentResolver?.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inpStr)
                    listBitmaps.add(bitmap)
                } catch (e : FileNotFoundException){
                    e.printStackTrace()
                }

            }
        }

         */

        /*
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                if (selectedImageUri != null) {
                    try {
                        val inputStream = requireActivity().contentResolver.openInputStream(selectedImageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        viewModel.pathImage = getPathFromUri(selectedImageUri)
                        Glide.with(context).load(viewModel.pathImage).override(1000, 1000).fitCenter().centerCrop().into(imageView)

                    } catch (exeption: Exception) {
                        Toast.makeText(context, "${exeption.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

         */

    }

    private fun setImages() {
        if(imageList != null){
            for (it in imageList!!.indices){
                Log.d("abbb", "$it")
            }
        }
    }

    fun getPathFromUri(contentUri: Uri): String {
        val filePath: String
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path!!
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }

        return filePath
    }

    fun setFontColor(colorPath: Int?) {
        when (colorPath) {
            1 -> {
                mess_addEditFrag.setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                mess_addEditFrag.setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                mess_addEditFrag.setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    fun setFontSize(colorSize: Int?) {
        when (colorSize) {
            1 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

            2 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }

            3 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
            }

            4 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
            }

            5 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
            }
        }
    }

    override fun onDestroyView() {
        Log.d("onDepp", "OnDestroy")

        val title = title_addEditFrag.text
        val message = mess_addEditFrag.text
        val pathImage = viewModel.pathImage
        val fontSize = viewModel.selectedFontSize
        val fontColor = viewModel.selectedFontNote
        val date = viewModel.noteDate
        val color = viewModel.selectedNoteColor
        val id = viewModel.idNote
        val isFavourite = viewModel.isFavourite
        val hasPassword = viewModel.hasPassword
        val password = viewModel.password

        val note = Note(title.toString(), message.toString(), date, false, color, pathImage, fontColor, fontSize, isFavourite, hasPassword, password)
        note.rowId = id

        viewModel.setSelectedNote(note)

        if(quit==2){
            setOffAddEdit()
        }

        super.onDestroyView()
    }

    fun setOffAddEdit(){
        selectedImagePath = ""
        title_addEditFrag.setText("")
        mess_addEditFrag.setText("")
        viewModel.setSelectedNote(null)
        viewModel.setSelectedNoteBeforeChange(null)
        viewModel.noteTitle = ""
        viewModel.noteMessage=""
        viewModel.noteDate = 1
        viewModel.pathImage = arrayListOf()
        viewModel.selectedFontSize = 3
        viewModel.selectedFontNote = 1
        viewModel.selectedNoteColor = "#333333"
        viewModel.idNote = -1
        viewModel.isFavourite = false
        viewModel.hasPassword = false
        viewModel.password = 0
    }

    fun setFavourite(boolean: Boolean){
        if(boolean){
            favouriteImage.setColorFilter(Color.parseColor("#FDBE3B"))
        } else{
            favouriteImage.setColorFilter(Color.WHITE)
        }
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if(hasPassword){
            imageViewPassword.setColorFilter(Color.parseColor("#FF4343"))
            imageViewPassword.setImageResource(R.drawable.ic_lock_24)
        } else{
            imageViewPassword.setColorFilter(Color.WHITE)
            imageViewPassword.setImageResource(R.drawable.ic_baseline_lock)
        }
    }
}
