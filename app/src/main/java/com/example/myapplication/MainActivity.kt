package com.example.myapplication

//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.Matrix
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.util.Size
//import android.view.Surface
import android.view.TextureView
//import android.view.ViewGroup
//import android.widget.Toast
import androidx.camera.core.*
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import kotlinx.android.synthetic.main.activity_main.*
//import java.io.File
//import java.util.concurrent.Executors
//
//
//private const val REQUEST_CODE_PERMISSIONS = 10
//private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
//import com.example.test_app.databinding.ActivityMainBinding
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import android.widget.Button
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale
////
////class MainActivity : AppCompatActivity(), LifecycleOwner {
////    private val executor = Executors.newSingleThreadExecutor()
////    private lateinit var viewFinder: TextureView
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_main)
////
////        viewFinder = findViewById(R.id.view_finder)
////
////        // カメラ起動
////        activateCameraBtn.setOnClickListener {
////            if (allPermissionsGranted()) {
////                viewFinder.post { startCamera() }
////            } else {
////                ActivityCompat.requestPermissions(
////                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
////                )
////            }
////        }
////
////        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
////            updateTransform()
////        }
////    }
////
////    private fun startCamera() {
////
////        //プレビューuseCaseの実装
//////        val previewConfig = Preview.Builder().apply {
//////            setTargetResolution(Size(viewFinder.width, viewFinder.height))
//////        }.build()
//////
//////        val preview = Preview(previewConfig)
////
////        preview=Preview.Builder
////            .build()
////        preview.setOnPreviewOutputUpdateListener {
////            val parent = viewFinder.parent as ViewGroup
////            parent.removeView(viewFinder)
////            parent.addView(viewFinder, 0)
////            viewFinder.surfaceTexture = it.surfaceTexture
////            updateTransform()
////        }
////
////        // 画像解析useCaseの実装
////        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
////            setImageReaderMode(
////                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
////            )
////        }.build()
////
//        //インスタンス
////        val imageAnalyzer = ImageAnalyze(applicationContext)
////        //推論結果を表示
////        imageAnalyzer.setOnAnalyzeListener(object : ImageAnalyze.OnAnalyzeListener {
////            override fun getAnalyzeResult(inferredCategory: String, score: Float) {
////                // メインスレッド以外からviewの変更をする
////                viewFinder.post {
////                    inferredCategoryText.text = "推論結果: $inferredCategory"
////                    inferredScoreText.text = "スコア: $score"
////                }
////            }
////        })
////        val analyzerUseCase = ImageAnalysis(analyzerConfig).apply {
////            setAnalyzer(executor, imageAnalyzer)
////        }
//
////        // useCaseはプレビューと画像解析
////        CameraX.bindToLifecycle(this, preview, analyzerUseCase)
////    }
////
////    private fun updateTransform() {
////        val matrix = Matrix()
////        val centerX = viewFinder.width / 2f
////        val centerY = viewFinder.height / 2f
////
////        val rotationDegrees = when (viewFinder.display.rotation) {
////            Surface.ROTATION_0 -> 0
////            Surface.ROTATION_90 -> 90
////            Surface.ROTATION_180 -> 180
////            Surface.ROTATION_270 -> 270
////            else -> return
////        }
////        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
////
////        //textureViewに反映
////        viewFinder.setTransform(matrix)
////    }
////
////    override fun onRequestPermissionsResult(
////        requestCode: Int, permissions: Array<String>, grantResults: IntArray
////    ) {
////        if (requestCode == REQUEST_CODE_PERMISSIONS) {
////            if (allPermissionsGranted()) {
////                viewFinder.post { startCamera() }
////            } else {
////                Toast.makeText(
////                    this,
////                    "Permissions not granted by the user.",
////                    Toast.LENGTH_SHORT
////                ).show()
////                finish()
////            }
////        }
////    }
////
////    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
////        ContextCompat.checkSelfPermission(
////            baseContext, it
////        ) == PackageManager.PERMISSION_GRANTED
////    }
////}

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val TAG = "test_app"
    }

//    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        viewBinding.imageCaptureButton.setOnClickListener { //Toast.makeText(this, "クリックされました。", Toast.LENGTH_SHORT).show()}
            takePhoto() }
//        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({ bindPreview(cameraProviderFuture.get()) }, ContextCompat.getMainExecutor(this))

//        //インスタンス
//        val imageAnalyzer = ImageAnalyze(applicationContext)
//        //推論結果を表示
//        imageAnalyzer.setOnAnalyzeListener(object : ImageAnalyze.OnAnalyzeListener {
//            override fun getAnalyzeResult(inferredCategory: String, score: Float) {
//                // メインスレッド以外からviewの変更をする
//                viewFinder.post {
//                    inferredCategoryText.text = "推論結果: $inferredCategory"
//                    inferredScoreText.text = "スコア: $score"
//                }
//            }
//        })
    }

    private fun takePhoto() {
//        Toast.makeText(this, "クリックされました。", Toast.LENGTH_SHORT).show()
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    Toast.makeText(baseContext, "クリックされました。", Toast.LENGTH_SHORT).show()
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        preview.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
        imageCapture = ImageCapture.Builder()
            .build()
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture)

        } catch(exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
//
//import android.Manifest
//import android.content.ContentValues
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.provider.MediaStore
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.ImageCapture
//import androidx.camera.video.Recorder
//import androidx.camera.video.Recording
//import androidx.camera.video.VideoCapture
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.test_app.databinding.ActivityMainBinding
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//import android.widget.Toast
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.core.Preview
//import androidx.camera.core.CameraSelector
//import android.util.Log
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.ImageProxy
//import androidx.camera.video.FallbackStrategy
//import androidx.camera.video.MediaStoreOutputOptions
//import androidx.camera.video.Quality
//import androidx.camera.video.QualitySelector
//import androidx.camera.video.VideoRecordEvent
//import androidx.core.content.PermissionChecker
//import java.nio.ByteBuffer
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//typealias LumaListener = (luma: Double) -> Unit
//
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var viewBinding: ActivityMainBinding
//
//    private var imageCapture: ImageCapture? = null
//
//    private var videoCapture: VideoCapture<Recorder>? = null
//    private var recording: Recording? = null
//
//    private lateinit var cameraExecutor: ExecutorService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
//
//        // Request camera permissions
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            ActivityCompat.requestPermissions(
//                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        }
//
//        // Set up the listeners for take photo and video capture buttons
//        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
//        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//    }
//
//    private fun takePhoto() {
//        // Get a stable reference of the modifiable image capture use case
//        val imageCapture = imageCapture ?: return
//
//        // Create time stamped name and MediaStore entry.
//        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
//            .format(System.currentTimeMillis())
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
//            }
//        }
//
//        // Create output options object which contains file + metadata
//        val outputOptions = ImageCapture.OutputFileOptions
//            .Builder(contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                contentValues)
//            .build()
//
//        // Set up image capture listener, which is triggered after photo has
//        // been taken
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
//                }
//
//                override fun
//                        onImageSaved(output: ImageCapture.OutputFileResults){
//                    val msg = "Photo capture succeeded: ${output.savedUri}"
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
//                }
//            }
//        )
//    }
//
//    private fun captureVideo() {}
//
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
////            val preview = Preview.Builder().build()
//            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
////            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
////            cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
//            // Used to bind the lifecycle of cameras to the lifecycle owner
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            // Preview
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
//                }
//
//            // Select back camera as a default
////            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                // Unbind use cases before rebinding
//                cameraProvider.unbindAll()
//
//                // Bind use cases to camera
//                cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview)
//
//            } catch(exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(
//            baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraExecutor.shutdown()
//    }
//
//    companion object {
//        private const val TAG = "CameraXApp"
//        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
//        private const val REQUEST_CODE_PERMISSIONS = 10
//        private val REQUIRED_PERMISSIONS =
//            mutableListOf (
//                Manifest.permission.CAMERA,
//                Manifest.permission.RECORD_AUDIO
//            ).apply {
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                }
//            }.toTypedArray()
//    }
//}