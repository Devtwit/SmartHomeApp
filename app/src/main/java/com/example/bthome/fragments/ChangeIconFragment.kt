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
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
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
                    binding.poojaLayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
                    binding.kitchenlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
                    binding.halllayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
                    binding.storeRoom.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
                    binding.studylayout.setCardBackgroundColor(Color.parseColor("#BAC0E1"))
                    binding.bedroomlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
                    binding.poojaSelected.visibility = View.VISIBLE
                    binding.kitchenSelected.visibility = View.VISIBLE
                    binding.bedSelected.visibility = View.GONE
                    binding.kitchen1Selected.visibility = View.VISIBLE
                    binding.kitchen2Selected.visibility = View.VISIBLE
                    binding.kitchen3Selected.visibility = View.VISIBLE
                    binding.kitchen4Selected.visibility = View.VISIBLE
                    binding.kitchen5Selected.visibility = View.VISIBLE
                    binding.kitchen6Selected.visibility = View.VISIBLE
                    binding.kitchen7Selected.visibility = View.VISIBLE
                    binding.kitchen8Selected.visibility = View.VISIBLE
                    binding.kitchen9Selected.visibility = View.VISIBLE
                    binding.kitchen10Selected.visibility = View.VISIBLE
                    binding.kitchen11Selected.visibility = View.VISIBLE
                    binding.kitchen12Selected.visibility = View.VISIBLE
                    binding.kitchen13Selected.visibility = View.VISIBLE
                    binding.kitchen14Selected.visibility = View.VISIBLE
                    binding.kitchen15Selected.visibility = View.VISIBLE
                    binding.kitchen16Selected.visibility = View.VISIBLE
                    binding.kitchen17Selected.visibility = View.VISIBLE
                    binding.kitchen18Selected.visibility = View.VISIBLE
                    binding.kitchen19Selected.visibility = View.VISIBLE
                    binding.kitchen20Selected.visibility = View.VISIBLE
                    binding.kitchen21Selected.visibility = View.VISIBLE
                    binding.kitchen22Selected.visibility = View.VISIBLE
                    binding.hallSelected.visibility = View.VISIBLE
                    binding.storeSelected.visibility = View.VISIBLE
                    binding.studySelected.visibility = View.VISIBLE

                    // Load the vector drawable from resources
                    drawableToBitmap(R.drawable.bed_1)
//             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.bedroom)
            }
        binding.kitchenlayout.setOnClickListener {
            binding.poojaLayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.kitchenlayout.setCardBackgroundColor(Color.parseColor("#BAC0E1"))
            binding.halllayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.storeRoom.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.studylayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.bedroomlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.poojaSelected.visibility = View.VISIBLE
            binding.kitchenSelected.visibility = View.GONE
            binding.bedSelected.visibility = View.VISIBLE
            binding.kitchen1Selected.visibility = View.VISIBLE
            binding.kitchen2Selected.visibility = View.VISIBLE
            binding.kitchen3Selected.visibility = View.VISIBLE
            binding.kitchen4Selected.visibility = View.VISIBLE
            binding.kitchen5Selected.visibility = View.VISIBLE
            binding.kitchen6Selected.visibility = View.VISIBLE
            binding.kitchen7Selected.visibility = View.VISIBLE
            binding.kitchen8Selected.visibility = View.VISIBLE
            binding.kitchen9Selected.visibility = View.VISIBLE
            binding.kitchen10Selected.visibility = View.VISIBLE
            binding.kitchen11Selected.visibility = View.VISIBLE
            binding.kitchen12Selected.visibility = View.VISIBLE
            binding.kitchen13Selected.visibility = View.VISIBLE
            binding.kitchen14Selected.visibility = View.VISIBLE
            binding.kitchen15Selected.visibility = View.VISIBLE
            binding.kitchen16Selected.visibility = View.VISIBLE
            binding.kitchen17Selected.visibility = View.VISIBLE
            binding.kitchen18Selected.visibility = View.VISIBLE
            binding.kitchen19Selected.visibility = View.VISIBLE
            binding.kitchen20Selected.visibility = View.VISIBLE
            binding.kitchen21Selected.visibility = View.VISIBLE
            binding.kitchen22Selected.visibility = View.VISIBLE
            binding.hallSelected.visibility = View.VISIBLE
            binding.storeSelected.visibility = View.VISIBLE
            binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
            drawableToBitmap(R.drawable.soup_kitchen)
//             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.kitchen)
           }
        binding.halllayout.setOnClickListener {
            binding.poojaLayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.kitchenlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.halllayout.setCardBackgroundColor(Color.parseColor("#BAC0E1"))
            binding.storeRoom.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.studylayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.bedroomlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.poojaSelected.visibility = View.VISIBLE
            binding.kitchenSelected.visibility = View.VISIBLE
            binding.bedSelected.visibility = View.VISIBLE
            binding.kitchen1Selected.visibility = View.VISIBLE
            binding.kitchen2Selected.visibility = View.VISIBLE
            binding.kitchen3Selected.visibility = View.VISIBLE
            binding.kitchen4Selected.visibility = View.VISIBLE
            binding.kitchen5Selected.visibility = View.VISIBLE
            binding.kitchen6Selected.visibility = View.VISIBLE
            binding.kitchen7Selected.visibility = View.VISIBLE
            binding.kitchen8Selected.visibility = View.VISIBLE
            binding.kitchen9Selected.visibility = View.VISIBLE
            binding.kitchen10Selected.visibility = View.VISIBLE
            binding.kitchen11Selected.visibility = View.VISIBLE
            binding.kitchen12Selected.visibility = View.VISIBLE
            binding.kitchen13Selected.visibility = View.VISIBLE
            binding.kitchen14Selected.visibility = View.VISIBLE
            binding.kitchen15Selected.visibility = View.VISIBLE
            binding.kitchen16Selected.visibility = View.VISIBLE
            binding.kitchen17Selected.visibility = View.VISIBLE
            binding.kitchen18Selected.visibility = View.VISIBLE
            binding.kitchen19Selected.visibility = View.VISIBLE
            binding.kitchen20Selected.visibility = View.VISIBLE
            binding.kitchen21Selected.visibility = View.VISIBLE
            binding.kitchen22Selected.visibility = View.VISIBLE
            binding.hallSelected.visibility = View.GONE
            binding.storeSelected.visibility = View.VISIBLE
            binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
            drawableToBitmap(R.drawable.balcony)
//             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.hall)
           }
        binding.studylayout.setOnClickListener {
            binding.poojaLayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.kitchenlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.halllayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.storeRoom.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.studylayout.setCardBackgroundColor(Color.parseColor("#BAC0E1"))
            binding.bedroomlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))

            binding.poojaSelected.visibility = View.VISIBLE
            binding.kitchenSelected.visibility = View.VISIBLE
            binding.bedSelected.visibility = View.VISIBLE
            binding.kitchen1Selected.visibility = View.VISIBLE
            binding.kitchen2Selected.visibility = View.VISIBLE
            binding.kitchen3Selected.visibility = View.VISIBLE
            binding.kitchen4Selected.visibility = View.VISIBLE
            binding.kitchen5Selected.visibility = View.VISIBLE
            binding.kitchen6Selected.visibility = View.VISIBLE
            binding.kitchen7Selected.visibility = View.VISIBLE
            binding.kitchen8Selected.visibility = View.VISIBLE
            binding.kitchen9Selected.visibility = View.VISIBLE
            binding.kitchen10Selected.visibility = View.VISIBLE
            binding.kitchen11Selected.visibility = View.VISIBLE
            binding.kitchen12Selected.visibility = View.VISIBLE
            binding.kitchen13Selected.visibility = View.VISIBLE
            binding.kitchen14Selected.visibility = View.VISIBLE
            binding.kitchen15Selected.visibility = View.VISIBLE
            binding.kitchen16Selected.visibility = View.VISIBLE
            binding.kitchen17Selected.visibility = View.VISIBLE
            binding.kitchen18Selected.visibility = View.VISIBLE
            binding.kitchen19Selected.visibility = View.VISIBLE
            binding.kitchen20Selected.visibility = View.VISIBLE
            binding.kitchen21Selected.visibility = View.VISIBLE
            binding.kitchen22Selected.visibility = View.VISIBLE
            binding.hallSelected.visibility = View.VISIBLE
            binding.storeSelected.visibility = View.VISIBLE
            binding.studySelected.visibility = View.GONE

            // Load the vector drawable from resources
            drawableToBitmap(R.drawable.local_library)
//             imageBitmap = BitmapFactory.decodeResource(requireContext().resources, R.drawable.study)
            }
        binding.storeRoom.setOnClickListener {
            binding.poojaLayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.kitchenlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.halllayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.storeRoom.setCardBackgroundColor(Color.parseColor("#BAC0E1"))
            binding.studylayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.bedroomlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.poojaSelected.visibility = View.VISIBLE
            binding.kitchenSelected.visibility = View.VISIBLE
            binding.bedSelected.visibility = View.VISIBLE
            binding.kitchen1Selected.visibility = View.VISIBLE
            binding.kitchen2Selected.visibility = View.VISIBLE
            binding.kitchen3Selected.visibility = View.VISIBLE
            binding.kitchen4Selected.visibility = View.VISIBLE
            binding.kitchen5Selected.visibility = View.VISIBLE
            binding.kitchen6Selected.visibility = View.VISIBLE
            binding.kitchen7Selected.visibility = View.VISIBLE
            binding.kitchen8Selected.visibility = View.VISIBLE
            binding.kitchen9Selected.visibility = View.VISIBLE
            binding.kitchen10Selected.visibility = View.VISIBLE
            binding.kitchen11Selected.visibility = View.VISIBLE
            binding.kitchen12Selected.visibility = View.VISIBLE
            binding.kitchen13Selected.visibility = View.VISIBLE
            binding.kitchen14Selected.visibility = View.VISIBLE
            binding.kitchen15Selected.visibility = View.VISIBLE
            binding.kitchen16Selected.visibility = View.VISIBLE
            binding.kitchen17Selected.visibility = View.VISIBLE
            binding.kitchen18Selected.visibility = View.VISIBLE
            binding.kitchen19Selected.visibility = View.VISIBLE
            binding.kitchen20Selected.visibility = View.VISIBLE
            binding.kitchen21Selected.visibility = View.VISIBLE
            binding.kitchen22Selected.visibility = View.VISIBLE
            binding.hallSelected.visibility = View.VISIBLE
            binding.storeSelected.visibility = View.GONE
            binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
            drawableToBitmap(R.drawable.dry_cleaning)
        }
        binding.poojaLayout.setOnClickListener {
            binding.poojaLayout.setCardBackgroundColor(Color.parseColor("#BAC0E1"))
            binding.kitchenlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.halllayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.storeRoom.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.studylayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))
            binding.bedroomlayout.setCardBackgroundColor(Color.parseColor("#2f2e3e"))

           binding.poojaSelected.visibility = View.GONE
           binding.kitchenSelected.visibility = View.VISIBLE
           binding.bedSelected.visibility = View.VISIBLE
           binding.kitchen1Selected.visibility = View.VISIBLE
           binding.kitchen2Selected.visibility = View.VISIBLE
           binding.kitchen3Selected.visibility = View.VISIBLE
           binding.kitchen4Selected.visibility = View.VISIBLE
           binding.kitchen5Selected.visibility = View.VISIBLE
           binding.kitchen6Selected.visibility = View.VISIBLE
           binding.kitchen7Selected.visibility = View.VISIBLE
           binding.kitchen8Selected.visibility = View.VISIBLE
           binding.kitchen9Selected.visibility = View.VISIBLE
           binding.kitchen10Selected.visibility = View.VISIBLE
           binding.kitchen11Selected.visibility = View.VISIBLE
           binding.kitchen12Selected.visibility = View.VISIBLE
           binding.kitchen13Selected.visibility = View.VISIBLE
           binding.kitchen14Selected.visibility = View.VISIBLE
           binding.kitchen15Selected.visibility = View.VISIBLE
           binding.kitchen16Selected.visibility = View.VISIBLE
           binding.kitchen17Selected.visibility = View.VISIBLE
           binding.kitchen18Selected.visibility = View.VISIBLE
           binding.kitchen19Selected.visibility = View.VISIBLE
           binding.kitchen20Selected.visibility = View.VISIBLE
           binding.kitchen21Selected.visibility = View.VISIBLE
           binding.kitchen22Selected.visibility = View.VISIBLE
           binding.hallSelected.visibility = View.VISIBLE
           binding.storeSelected.visibility = View.VISIBLE
           binding.studySelected.visibility = View.VISIBLE

            // Load the vector drawable from resources
          drawableToBitmap(R.drawable.diya_1)


        }
        binding.nextButton.setOnClickListener {
            DatabaseHelper(requireContext()).updateLocationImage("BT-Beacon_room1", imageBitmap!! )
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeIconFragment_to_addBleDeviceFragment)

        }
        binding.skipButton.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_changeIconFragment_to_dataBaseUpdateFragment)
        }
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
                Color.parseColor("#000000")
            ) // Replace with your desired tint color
            tintedDrawable.draw(canvas)

            // Now, 'bitmap' contains the converted bitmap
            imageBitmap = bitmap
        }

    }

}