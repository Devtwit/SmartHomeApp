package com.example.bthome.fragments

import DatabaseHelper
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
         imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_catering_back1_logo)

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
                binding.selectImageView1.setImageBitmap(croppedBitmap)
                binding.selectImageViewSelected.setImageBitmap(croppedBitmap)
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
        binding.selectedImageView.setOnClickListener {
            // Open the image picker when the button is clicked
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
            binding.selectedImageView.visibility = View.VISIBLE

        }
                binding.bedroomlayout.setOnClickListener {
//                    binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//                    binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//                    binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//                    binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//                    binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//                    binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//                    binding.poojaSelected.visibility = View.VISIBLE
//                    binding.kitchenSelected.visibility = View.VISIBLE
//                    binding.bedSelected.visibility = View.GONE
//                    binding.hallselected.visibility = View.VISIBLE
//                    binding.storeSelected.visibility = View.VISIBLE
//                    binding.studySelected.visibility = View.VISIBLE
binding.bedroom.setBackgroundResource(R.drawable.card_shadow)
                    binding.pooja.setBackgroundResource(R.drawable.card_shadow_back)
                    binding.kitchen.setBackgroundResource(R.drawable.card_shadow_back)
                    binding.study.setBackgroundResource(R.drawable.card_shadow_back)
                    binding.store.setBackgroundResource(R.drawable.card_shadow_back)
                    binding.hall.setBackgroundResource(R.drawable.card_shadow_back)
                    binding.car.setBackgroundResource(R.drawable.card_shadow_back)
                    binding.bath.setBackgroundResource(R.drawable.card_shadow_back)
                    // Load the vector drawable from resources
//                    drawableToBitmap(R.drawable.mono_bed_back)
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_bed_logo)
            }
        binding.kitchenlayout.setOnClickListener {
//            binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//            binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.poojaSelected.visibility = View.VISIBLE
//            binding.kitchenSelected.visibility = View.GONE
//            binding.bedSelected.visibility = View.VISIBLE
//            binding.hallselected.visibility = View.VISIBLE
//            binding.storeSelected.visibility = View.VISIBLE
//            binding.studySelected.visibility = View.VISIBLE
            binding.kitchen.setBackgroundResource(R.drawable.card_shadow)
            binding.pooja.setBackgroundResource(R.drawable.card_shadow_back)
            binding.study.setBackgroundResource(R.drawable.card_shadow_back)
            binding.store.setBackgroundResource(R.drawable.card_shadow_back)
            binding.hall.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bedroom.setBackgroundResource(R.drawable.card_shadow_back)
            binding.car.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bath.setBackgroundResource(R.drawable.card_shadow_back)
            // Load the vector drawable from resources
//            drawableToBitmap(R.drawable.mono_catering_back1)
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_catering_back1_logo)
           }
        binding.halllayout.setOnClickListener {
//            binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//            binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.poojaSelected.visibility = View.VISIBLE
//            binding.kitchenSelected.visibility = View.VISIBLE
//            binding.bedSelected.visibility = View.VISIBLE
//            binding.hallselected.visibility = View.GONE
//            binding.storeSelected.visibility = View.VISIBLE
//            binding.studySelected.visibility = View.VISIBLE
            binding.hall.setBackgroundResource(R.drawable.card_shadow)
            binding.pooja.setBackgroundResource(R.drawable.card_shadow_back)
            binding.kitchen.setBackgroundResource(R.drawable.card_shadow_back)
            binding.study.setBackgroundResource(R.drawable.card_shadow_back)
            binding.store.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bedroom.setBackgroundResource(R.drawable.card_shadow_back)
            binding.car.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bath.setBackgroundResource(R.drawable.card_shadow_back)
            // Load the vector drawable from resources
//            drawableToBitmap(R.drawable.mono_hall)
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_hall_logo)
           }
        binding.studylayout.setOnClickListener {
//            binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//            binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//
//            binding.poojaSelected.visibility = View.VISIBLE
//            binding.kitchenSelected.visibility = View.VISIBLE
//            binding.bedSelected.visibility = View.VISIBLE
//            binding.hallselected.visibility = View.VISIBLE
//            binding.storeSelected.visibility = View.VISIBLE
//            binding.studySelected.visibility = View.GONE
            binding.study.setBackgroundResource(R.drawable.card_shadow)
            binding.pooja.setBackgroundResource(R.drawable.card_shadow_back)
            binding.kitchen.setBackgroundResource(R.drawable.card_shadow_back)
            binding.store.setBackgroundResource(R.drawable.card_shadow_back)
            binding.hall.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bedroom.setBackgroundResource(R.drawable.card_shadow_back)
            binding.car.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bath.setBackgroundResource(R.drawable.card_shadow_back)
            // Load the vector drawable from resources
//            drawableToBitmap(R.drawable.mono_child)
             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_child_logo)

            }
        binding.storeRoom.setOnClickListener {
//            binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//            binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.poojaSelected.visibility = View.VISIBLE
//            binding.kitchenSelected.visibility = View.VISIBLE
//            binding.bedSelected.visibility = View.VISIBLE
            binding.store.setBackgroundResource(R.drawable.card_shadow)
            binding.pooja.setBackgroundResource(R.drawable.card_shadow_back)
            binding.kitchen.setBackgroundResource(R.drawable.card_shadow_back)
            binding.study.setBackgroundResource(R.drawable.card_shadow_back)
            binding.hall.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bedroom.setBackgroundResource(R.drawable.card_shadow_back)
            binding.car.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bath.setBackgroundResource(R.drawable.card_shadow_back)
//            binding.hallselected.visibility = View.VISIBLE
//            binding.storeSelected.visibility = View.GONE
//            binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
//            drawableToBitmap(R.drawable.mono_storage)
            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_storage_logo)
        }
        binding.poojaLayout.setOnClickListener {
//            binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//            binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
            binding.pooja.setBackgroundResource(R.drawable.card_shadow)
            binding.kitchen.setBackgroundResource(R.drawable.card_shadow_back)
            binding.study.setBackgroundResource(R.drawable.card_shadow_back)
            binding.store.setBackgroundResource(R.drawable.card_shadow_back)
            binding.hall.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bedroom.setBackgroundResource(R.drawable.card_shadow_back)
            binding.car.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bath.setBackgroundResource(R.drawable.card_shadow_back)

//           binding.poojaSelected.visibility = View.GONE
//           binding.kitchenSelected.visibility = View.VISIBLE
//           binding.bedSelected.visibility = View.VISIBLE
//           binding.hallselected.visibility = View.VISIBLE
//           binding.storeSelected.visibility = View.VISIBLE
//           binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
//          drawableToBitmap(R.drawable.mono_office)
            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_office_logo)

        }
        binding.bathLayout.setOnClickListener {
//            binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//            binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
            binding.pooja.setBackgroundResource(R.drawable.card_shadow_back)
            binding.kitchen.setBackgroundResource(R.drawable.card_shadow_back)
            binding.study.setBackgroundResource(R.drawable.card_shadow_back)
            binding.store.setBackgroundResource(R.drawable.card_shadow_back)
            binding.hall.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bedroom.setBackgroundResource(R.drawable.card_shadow_back)
            binding.car.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bath.setBackgroundResource(R.drawable.card_shadow)

//           binding.poojaSelected.visibility = View.GONE
//           binding.kitchenSelected.visibility = View.VISIBLE
//           binding.bedSelected.visibility = View.VISIBLE
//           binding.hallselected.visibility = View.VISIBLE
//           binding.storeSelected.visibility = View.VISIBLE
//           binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
//            drawableToBitmap(R.drawable.mono_bath)
            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_bath_logo1)

        }
        binding.carLayout.setOnClickListener {
//            binding.poojaLayout.setCardBackgroundColor(requireContext().getColor(R.color.light_water_))
//            binding.kitchenlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.halllayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.storeRoom.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.studylayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
//            binding.bedroomlayout.setCardBackgroundColor(requireContext().getColor(R.color.back_color))
            binding.pooja.setBackgroundResource(R.drawable.card_shadow_back)
            binding.kitchen.setBackgroundResource(R.drawable.card_shadow_back)
            binding.study.setBackgroundResource(R.drawable.card_shadow_back)
            binding.store.setBackgroundResource(R.drawable.card_shadow_back)
            binding.hall.setBackgroundResource(R.drawable.card_shadow_back)
            binding.bedroom.setBackgroundResource(R.drawable.card_shadow_back)
            binding.car.setBackgroundResource(R.drawable.card_shadow)
            binding.bath.setBackgroundResource(R.drawable.card_shadow_back)

//           binding.poojaSelected.visibility = View.GONE
//           binding.kitchenSelected.visibility = View.VISIBLE
//           binding.bedSelected.visibility = View.VISIBLE
//           binding.hallselected.visibility = View.VISIBLE
//           binding.storeSelected.visibility = View.VISIBLE
//           binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
//            drawableToBitmap(R.drawable.mono_carparking)
            imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.mono_carparking_logo)


        }
        binding.nextButton.setOnClickListener {

            var oldName = AddBleDeviceFragment.responseAdapter.getString(MoreFragment.idValue.toInt()).toString()
            if(oldName.isEmpty()){
                DatabaseHelper(requireContext()).updateLocationImage("", imageBitmap!!)
            } else {
                DatabaseHelper(requireContext()).updateLocationImage(oldName, imageBitmap!!)
            }
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeIconFragment_to_addBleDeviceFragment)
//            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeIconFragment_to_mainFragment)

        }
//        binding.skipButton.setOnClickListener {
//            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeIconFragment_to_dataBaseUpdateFragment)
//        }
    }

    private fun drawableToBitmap(drawableImage: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableImage)

// Ensure that the drawable is not null
        if (drawable != null) {
            // Create a bitmap with the same dimensions as the drawable
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            // Create a canvas to draw the drawable onto the bitmap
            val canvas = Canvas(bitmap)

            // Draw the drawable onto the canvas
            drawable.setBounds(0, 0, canvas.width, canvas.height)

            // If the drawable is a vector drawable, you might need to tint it if it contains colors
            val tintedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(
                tintedDrawable,
//                Color.parseColor("#808080")
//                Color.parseColor("#000000")
                requireContext().getColor(R.color.back_color)
            ) // Replace with your desired tint color
            tintedDrawable.draw(canvas)

            // Now, 'bitmap' contains the converted bitmap
            imageBitmap = bitmap
        }

    }

}