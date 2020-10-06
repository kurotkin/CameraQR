package com.kurotkin.cameraqr.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat.getSystemService
import com.kurotkin.cameraqr.CameraQR

object VibroUtil {
    fun vibro(t: Long = 500){
        val v = CameraQR.instance.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(t, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(t)
        }
    }
}