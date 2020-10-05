package com.kurotkin.cameraqr.scaners

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.kurotkin.cameraqr.R
import java.io.IOException

class GmsScanerActivity : AppCompatActivity() {

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var cameraView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gms_scaner)
        cameraView = findViewById<SurfaceView>(R.id.surface_view)
        scan()
    }

    fun scan(){
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

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes: SparseArray<Barcode>? = detections?.getDetectedItems()
                if (barcodes?.size() != 0) {
                    val code = barcodes?.valueAt(0)?.displayValue
                    code?.let {
                        scanResult(it)
                    }
                }
            }

        })
    }

    fun scanResult(code: String){
        val intent = Intent()
        intent.putExtra("code", code)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.release()
        barcodeDetector.release()
    }
}