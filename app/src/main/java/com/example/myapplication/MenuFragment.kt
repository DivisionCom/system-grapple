package com.example.myapplication

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private lateinit var bitmap: Bitmap
    private var uri: Uri? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var intent: Intent? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun initGalleryLauncher(){
        launcher = activity?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            uri = it.data?.data
            // Process result from Uri
            if (uri != null) {
                setWallpaperBitmap()
                Toast.makeText(context, "Success! Wait...", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "No media selected", Toast.LENGTH_SHORT).show()
            }
        }
        intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun setWallpaperBitmap() {
        val wallpaperManager = WallpaperManager.getInstance(context)
        bitmap =
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    requireContext().contentResolver,
                    uri!!
                )
            )
        wallpaperManager.setBitmap(bitmap)
    }

    private fun openGallery() {
        launcher?.launch(intent)
    }

    private fun init(){
        initGalleryLauncher()
        binding.btnSetWallpaper.setOnClickListener {
            try {
                openGallery()
            } catch (e: Exception) {
                Log.d("PhotoPicker", "Error: $e")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}