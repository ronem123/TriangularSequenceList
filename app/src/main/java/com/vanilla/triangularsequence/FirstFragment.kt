package com.vanilla.triangularsequence

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vanilla.triangularsequence.databinding.FragmentFirstBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private var dogUri: String? = null
    private var catUri: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        codeSmellForIfElseMerge(ListData("", ""))
        writeToFile(requireContext(),"filename","hello World")
        isAccessAvailable(2)

        validateUserInput("HelloWorld")
        validateDataInput("HelloWorld")

    }


    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun setClickListeners() {
        binding.btnSubmit.setOnClickListener {
            val tsValue = binding.editTSNumber.text.toString()
            if (tsValue.isEmpty()) {
                showMessage("Please enter No. of Triangular Sequence first")
            } else if (dogUri == null || catUri == null) {
                showMessage("You must pick both the dog and the cat image first to proceed")
            } else {
                findNavController().navigate(
                    FirstFragmentDirections.actionFirstFragmentToSecondFragment(
                        tsValue.toInt(),
                        dogUri.toString(),
                        catUri.toString()
                    )
                )
            }
        }

        binding.btnPickDog.setOnClickListener {
            selectedImageType = ImageType.Dog
            openPickMediaForImage()
        }

        binding.btnPickCat.setOnClickListener {
            selectedImageType = ImageType.Cat
            openPickMediaForImage()
        }
    }

    enum class ImageType {
        Dog, Cat
    }

    private lateinit var selectedImageType: ImageType
    private val galleryPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    private val requestGalleryPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->

            openPickMediaForImage()

        }


    private fun openPickMediaForImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                if (selectedImageType == ImageType.Dog) {
                    dogUri = uri.toString()
                    loadImage(dogUri!!, binding.imageViewDog)
                } else {
                    catUri = uri.toString()
                    loadImage(catUri!!, binding.imageViewCat)

                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private fun loadImage(uri: String, imageView: ImageView) {
        Glide.with(requireContext())
            .load(Uri.parse(uri))
            .into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun validateUserInput(input: String?) {
        require(!input.isNullOrEmpty()) { "Input cannot be empty" }
        require(input.matches("[a-zA-Z0-9]+".toRegex())) { "Input must contain only letters and numbers" }
    }

    fun validateDataInput(input: String?) {
        require(!input.isNullOrEmpty()) { "Data cannot be empty" }
        require(input.matches("[a-zA-Z0-9]+".toRegex())) { "Data must contain only letters and numbers" }
    }

    private fun codeSmellForIfElseMerge(data: ListData?) {
        if (data != null) {
            Log.v("TAG", "data was not null")
            //perform some task
            if (data != null) {
                Log.v("TAG", "data was not null again")
            }
        }
    }

    private fun isAccessAvailable(_count: Int): Boolean {
        var count = _count
        if (count == 0) {
            count++
            return true
        } else if (count == 2) {
            count++
            return true
        }

        return true
    }

   private fun writeToFile(context: Context, fileName: String, content: String) {
        try {
            // Get the internal storage directory where you want to store the file
            val directory = context.filesDir

            // Create a File object with the specified directory and file name
            val file = File(directory, fileName)

            // Open a FileOutputStream to write to the file
            val fileOutputStream = FileOutputStream(file)

            // Create an OutputStreamWriter to write characters to the FileOutputStream
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)

            // Write the content to the file
            outputStreamWriter.write(content)

        } catch (e: Exception) {
            // Handle exceptions, such as IOException or FileNotFoundException
            e.printStackTrace()
        }
    }
}