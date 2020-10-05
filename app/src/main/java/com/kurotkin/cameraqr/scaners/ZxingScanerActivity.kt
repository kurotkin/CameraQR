package com.kurotkin.cameraqr.scaners

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import com.kurotkin.cameraqr.R

class ZxingScanerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing_scaner)
        prepareScan()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    fun prepareScan(){
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.DATA_MATRIX)
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        //codeScanner.autoFocusMode = AutoFocusMode.CONTINUOUS // or CONTINUOUS
        codeScanner.setAutoFocusInterval(200)
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                vibro()
                //Toast.makeText(this, "${it.text}", Toast.LENGTH_LONG).show()
                scanResult(it.text)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Ошибка камеры: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    fun scanResult(code: String){
        val intent = Intent()
        intent.putExtra("code", code)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun vibro(t: Long = 500){
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(t, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(t)
        }
    }
}