package com.vanilla.triangularsequence

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vanilla.triangularsequence.databinding.FragmentFirstBinding

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

        initializeViews()
        setClickListeners()

    }

    private fun initializeViews() {

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
//            checkGalleryPermissionAndLaunchPickerFor()
            openPickMediaForImage()
        }

        binding.btnPickCat.setOnClickListener {
            selectedImageType = ImageType.Cat
//            checkGalleryPermissionAndLaunchPickerFor()
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
        ) { isGranted: Boolean ->
//            if (isGranted) {
//                showMessage("Permission Granted")
                openPickMediaForImage()
//            } else {
//                showRationaleDialog("You have denied permission.")
//            }
        }

    private fun showRationaleDialog(preMessage: String = "") {
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setMessage("$preMessage You must give access permission for the gallery")
            .setPositiveButton("Give Permission") { d, _ ->
                openGalleryPermissionDialog()
                d.cancel()
            }
            .create().show()
    }

    private fun openGalleryPermissionDialog() {
        showMessage("Launching permission dialog")
        requestGalleryPermission.launch(galleryPermission)
    }

    private fun checkGalleryPermissionAndLaunchPickerFor() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                galleryPermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                showMessage("Permission already Granted")
                openPickMediaForImage()
            }
            shouldShowRequestPermissionRationale(galleryPermission) -> {
                val builder = AlertDialog.Builder(requireContext())
                builder
                    .setMessage("You must give access permission for the gallery")
                    .setPositiveButton("Give Permission") { d, _ ->
                        openGalleryPermissionDialog()
                        d.cancel()
                    }
                    .create().show()
            }
            else -> openGalleryPermissionDialog()
        }
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
}