package com.dicoding.asclepius.view.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.History
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.DateHelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.viewModel.MainViewModel
import com.dicoding.asclepius.view.viewModel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val mainViewModel by viewModels<MainViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu1 -> {
                    val intent = Intent(this@MainActivity, NewsActivity::class.java)
                    startActivity(intent)
                    true
                    }
                R.id.menu2 -> {
                    val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia ()
    ) {uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        }

    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
            binding.textTakePicture.text = ""
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        currentImageUri?.let { uri: Uri ->
            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
                    }

                    override fun onResults(results: List<Classifications>?) {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val result = it[0].categories[0]
                                val date = DateHelper.getCurrentDate()
                                val displayResult = "${result.label} " + NumberFormat.getPercentInstance().format(result.score)
                                val history = History(imageUri = uri.toString(), result = displayResult, date = date)
                                mainViewModel.insert(history)
                                moveToResult(displayResult, uri , date)
                            }
                        }
                    }

                }
            )

            imageClassifierHelper.classifyStaticImage(uri)
        } ?: showToast(getString(R.string.empty_image_warning))

    }

    private fun moveToResult(result: String, imageUri: Uri, date: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(EXTRA_IMAGE, imageUri.toString())
        intent.putExtra(EXTRA_RESULT, result)
        intent.putExtra(EXTRA_DATE, date)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "mainActivity"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_DATE = "extra_date"
    }
}