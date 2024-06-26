package com.example.smartlibrary1.fragments.library

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smartlibrary1.R
import com.example.smartlibrary1.adapters.BestBookAdapter
import com.example.smartlibrary1.databinding.FragmentSearchBinding
import com.example.smartlibrary1.util.Resource
import com.example.smartlibrary1.util.showBottomNavigationView
import com.example.smartlibrary1.viewmodel.SearchViewModel
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint

@Suppress("DEPRECATION")
class fragment_search : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 102
    private lateinit var textRecognizer: TextRecognizer
    private var imageUri: Uri? = null
    private lateinit var searchBookAdapter: BestBookAdapter
    private val args by navArgs<fragment_searchArgs>()

    private val viewModel by viewModels<SearchViewModel>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        textRecognizer = TextRecognizer.Builder(requireContext()).build()

        binding.captureButton.setOnClickListener {
            checkCameraPermissionAndCaptureImage()
//            SearchViewModel.searchBookList.clear()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchBookRV()

        searchBookAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("book",it)
                putInt("number",1)// để chọn dungf viewmodel nào
            }
            findNavController().navigate(R.id.action_fragment_search_to_fragment_book_detail,b)
        }

        lifecycleScope.launch {
            viewModel.specialBook.collectLatest {

                when (it) {
                    is Resource.Loading ->{

                    }

                    is Resource.Error -> TODO()
                    is Resource.Success -> {
                        searchBookAdapter.differ.submitList(it.data)

                    }
                    is Resource.Unspecified -> TODO()
                }
            }
        }
        val searchString = args.searchstring
        val linkSearchBook = constructSearchUrl(searchString)
        if (searchString != "") {
            viewModel.fetchSpecialBook(linkSearchBook,1)
            SearchViewModel.searchBookList.clear()
        }

    }
    private fun checkCameraPermissionAndCaptureImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.TITLE, "OCR_${System.currentTimeMillis()}")
                    put(MediaStore.Images.Media.DESCRIPTION, "Image capture for OCR")
                }
                val resolver = requireContext().contentResolver
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                imageUri?.let {
                    this.imageUri = it
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                } ?: run {
                    Log.e("OCRFragment", "Failed to create image file")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == androidx.fragment.app.FragmentActivity.RESULT_OK) {
            imageUri?.let { uri ->
                val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
                binding.imageView.setImageBitmap(bitmap)
                performOCR(bitmap)
            }
        }
    }

    private fun performOCR(bitmap: Bitmap) {
        if (!textRecognizer.isOperational) {
            binding.textView.text = "Text recognizer could not be set up on your device."
            return
        }

        val frame = Frame.Builder().setBitmap(bitmap).build()
        val textBlocks = textRecognizer.detect(frame)

        val stringBuilder = StringBuilder()
        for (i in 0 until textBlocks.size()) {
            val textBlock = textBlocks.valueAt(i)
            stringBuilder.append(textBlock.value)
            stringBuilder.append("\n")
        }

        binding.textView.text = stringBuilder.toString()
        val lines = stringBuilder.toString().split("\n")

        for (line in lines) {
            val a = constructSearchUrl(line)
            if (a == "https://nhatrangbooks.com/?s=&post_type=product") break
            viewModel.fetchSpecialBook(a,1)
            println(a)

        }






    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Log.e("OCRFragment", "Camera permission denied")
            }
        }
    }
    private fun constructSearchUrl(query: String): String {
        val baseUrl = "https://nhatrangbooks.com/?s="
        val modifiedQuery = query.replace(" ", "+")
        return "$baseUrl$modifiedQuery&post_type=product"
    }
    private fun setupSearchBookRV() {
        searchBookAdapter = BestBookAdapter()
        binding.rvSearchBook.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = searchBookAdapter
        }
    }
    override fun onResume() {

        super.onResume()
        showBottomNavigationView()
    }
}