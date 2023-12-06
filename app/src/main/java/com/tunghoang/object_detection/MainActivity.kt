package com.tunghoang.object_detection

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.frame.Frame

class MainActivity : AppCompatActivity() {
    private lateinit var cameraView: CameraView
    private lateinit var drawView: DrawView
    private lateinit var objectDetector: ObjectDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cameraView = findViewById(R.id.cameraView)
        drawView = findViewById(R.id.drawView)
//
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .build()

        objectDetector = ObjectDetection.getClient(options)

        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    setupCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    makeToast("Permissions Required!")
                }
            }).check()

    }

    private fun makeToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun setupCamera() {
        cameraView.setLifecycleOwner(this)
        cameraView.facing = Facing.BACK
        cameraView.addFrameProcessor { frame -> processImage(getInputImageFromFrame(frame)) }
    }

    private fun processImage(inputImage: InputImage) {
        objectDetector.process(inputImage)
            .addOnSuccessListener { detectedObjects: List<DetectedObject?>? ->
                processResults(
                    detectedObjects,
                    inputImage.width,
                    inputImage.height
                )
            }
            .addOnFailureListener { e: Exception? -> makeToast("Some Error Occurred") }
    }

    private fun processResults(detectedObjects: List<DetectedObject?>?, width: Int, height: Int) {
        detectedObjects?.let {
            for (i in it) {
                i?.let {
                    val boundingBox = i.boundingBox
                    var text = "Undefined"
                    if (i.labels.size != 0) {
                        text = i.labels[0].text
                    }
                    drawView.setData(boundingBox, text, width, height)
                }
            }
        }
    }

    private fun getInputImageFromFrame(frame: Frame): InputImage {
        val data = frame.getData<ByteArray>()
        return InputImage.fromByteArray(
            data,
            frame.size.width,
            frame.size.height,
            frame.rotationToView,
            InputImage.IMAGE_FORMAT_NV21
        )
    }
}