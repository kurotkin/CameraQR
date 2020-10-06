package com.kurotkin.cameraqr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.kurotkin.cameraqr.scaners.GmsScanerActivity
import com.kurotkin.cameraqr.scaners.ZxingScanerActivity
import com.kurotkin.cameraqr.utils.DateUtil
import com.kurotkin.cameraqr.utils.VibroUtil
import kotlinx.android.synthetic.main.activity_scaner.*
import java.util.*

class ScanerActivity : AppCompatActivity() {

    companion object{
        private val LABEL = "com.kurotkin.cameraqr.ScanerActivity.text"
        private val START_DATE = "com.kurotkin.cameraqr.ScanerActivity.startDate"
        private val SCANER1 = IntentIntegrator.REQUEST_CODE
        private val SCANER2 = 2
        private val SCANER3 = 3
    }

    lateinit var editText: EditText
    private var t1 = Date().time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scaner)
        editText = findViewById(R.id.editTextTextPersonName)

        savedInstanceState?.let {
            val text = it.getString(LABEL, "")
            editText.setText(text)
            t1 = it.getLong(START_DATE, Date().time)
        }

        button.setOnClickListener { runScan1() }
        button2.setOnClickListener { runScan2() }
        button3.setOnClickListener { runScan3() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LABEL, editText.text.toString())
        outState.putLong(START_DATE, t1)
    }

    fun runScan1(){
        t1 = Date().time
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Сканировать")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan()
    }

    fun runScan2(){
        t1 = Date().time
        val intent = Intent(this, ZxingScanerActivity::class.java)
        startActivityForResult(intent, SCANER2)
    }

    fun runScan3(){
        t1 = Date().time
        val intent = Intent(this, GmsScanerActivity::class.java)
        startActivityForResult(intent, SCANER3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode){
            SCANER1 -> {
                val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                if (result != null) {
                    if (result.contents == null) {

                    } else {
                        VibroUtil.vibro()
                        val code : String = result.contents
                        showSaveDialog(code, "Сканер 1")
                    }
                }
            }
            SCANER2 -> {
                if (data == null) return
                val code: String = data.getStringExtra("code")
                showSaveDialog(code, "Сканер 2")
            }

            SCANER3 -> {
                if (data == null) return
                val code: String = data.getStringExtra("code")
                showSaveDialog(code, "Сканер 3")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showSaveDialog(code: String, scaner: String) {
        val t2 = Date().time
        val td = t2 - t1
        val tdSec : Double = ((td.toInt() / 100).toDouble()) / 10.0
        val txt = "${editText.text}\n$scaner - ${DateUtil.getTime()} - ${tdSec}сек"
        editText.setText(txt)
        try {
            val ms = AlertDialog.Builder(this)
            ms.setTitle("Прочитана метка:")
            ms.setMessage("$code\nВремя сканирования ${tdSec} сек")
            ms.setPositiveButton("Да") { dialog, arg1 -> }
            ms.show()
        } catch (e: NullPointerException) {
            Log.e("Err", e.toString())
        }
    }

}