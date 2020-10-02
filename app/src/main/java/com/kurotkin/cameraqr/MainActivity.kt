package com.kurotkin.cameraqr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var cameraView: SurfaceView
    private lateinit var barcodeValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraView = findViewById<SurfaceView>(R.id.surface_view)
        barcodeValue = findViewById<TextView>(R.id.barcode_value)

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1027, 1024)
            .setAutoFocusEnabled(true)
            .setFocusMode("continuous-video")
            .build()



        cameraView.getHolder().addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    cameraSource.start(cameraView.holder)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                TODO("Not yet implemented")
            }

            override fun receiveDetections(detections: Detections<Barcode>?) {
                val barcodes: SparseArray<Barcode>? = detections?.getDetectedItems()
                if (barcodes?.size() != 0) {
                    barcodeValue.post {
                        barcodeValue.text = barcodes?.valueAt(0)?.displayValue
                    }
                }
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.release()
        barcodeDetector.release()
    }

}


