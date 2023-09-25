package com.example.bthome.fragments

import DatabaseHelper
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation

import com.example.bthome.R
import com.example.bthome.databinding.FragmentChangeIconBinding
import com.soundcloud.android.crop.CropImageView
import java.io.ByteArrayOutputStream
import java.io.InputStream


class ChangeIconFragment : Fragment() {


    private lateinit var binding: FragmentChangeIconBinding
    lateinit var imageBitmap :Bitmap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_icon, container, false)
         imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.poojadiya)

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "On Resume")
        setUpListener()
    }
    private val SELECT_IMAGE_REQUEST_CODE = 101


    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
companion object{
    private val TAG = ChangeIconFragment::class.java.simpleName
}

    private val CROP_IMAGE_REQUEST_CODE = 102

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let {
                // Start cropping activity
                val cropIntent = Intent("com.android.camera.action.CROP")
                cropIntent.setDataAndType(it, "image/*")
                cropIntent.putExtra("crop", "true")
                cropIntent.putExtra("aspectX", 1)
                cropIntent.putExtra("aspectY", 1)
                cropIntent.putExtra("outputX", 300) // Adjust the output size as needed (100x100)
                cropIntent.putExtra("outputY", 300)
                cropIntent.putExtra("return-data", true)

                startActivityForResult(cropIntent, CROP_IMAGE_REQUEST_CODE)
            }
        } else if (requestCode == CROP_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle the cropped image result
            val extras = data.extras
            if (extras != null) {
                val croppedBitmap = extras.getParcelable<Bitmap>("data")

                // Display the cropped image and save it to your database or perform other actions here
                binding.selectedImageView.setImageBitmap(croppedBitmap)
                imageBitmap = croppedBitmap!!

                // Save the cropped image to your database or perform other actions here
                val imageByteArray = convertBitmapToByteArray(croppedBitmap)

                // Call a function to save the image to your database
                // DatabaseHelper(requireContext()).updateLocationImage("BT-Beacon_room1", croppedBitmap!!)
                // saveImageToDatabase(imageByteArray)
            }
        }
    }

    fun setUpListener() {

        binding.imageButton.setOnClickListener{
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).popBackStack()
        }
    binding.selectImageButton.setOnClickListener {
            // Open the image picker when the button is clicked
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
            binding.selectedImageView.visibility = View.VISIBLE

    }
                binding.bedroomlayout.setOnClickListener {
                    binding.bedroom.setBackground(ContextCompat.getDrawable(context!!, R.drawable.selected_device_orange))
                    binding.study.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
                    binding.kitchen.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
                    binding.hall.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
                    binding.pooja.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
                    binding.store.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.bedroom)
            }
        binding.kitchenlayout.setOnClickListener {
            binding.kitchen.setBackground(ContextCompat.getDrawable(context!!, R.drawable.selected_device_orange))
            binding.study.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.bedroom.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.kitchen)
           }
        binding.halllayout.setOnClickListener {
            binding.hall.setBackground(ContextCompat.getDrawable(context!!, R.drawable.selected_device_orange))
            binding.study.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.bedroom.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.hall)
           }
        binding.studylayout.setOnClickListener {
            binding.study.setBackground(ContextCompat.getDrawable(context!!, R.drawable.selected_device_orange))
            binding.store.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.bedroom.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.study)
            }
        binding.storeRoom.setOnClickListener {
            binding.store.setBackground(ContextCompat.getDrawable(context!!, R.drawable.selected_device_orange))
            binding.study.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.pooja.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.bedroom.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.storeroom)
        }
        binding.poojaLayout.setOnClickListener {
            binding.pooja.setBackground(ContextCompat.getDrawable(context!!, R.drawable.selected_device_orange))
            binding.study.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.kitchen.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.hall.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.store.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
            binding.bedroom.setBackground(ContextCompat.getDrawable(context!!, R.drawable.select_device_unselected_bg))
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.poojadiya)
        }
        binding.nextButton.setOnClickListener {
            DatabaseHelper(requireContext()).updateLocationImage("BT-Beacon_room1", imageBitmap!! )
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeIconFragment_to_addBleDeviceFragment)

        }
        binding.skipButton.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeIconFragment_to_dataBaseUpdateFragment)
        }
    }

}