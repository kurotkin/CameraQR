package com.kurotkin.cameraqr

import android.app.Application
import com.kurotkin.cameraqr.dagger.AppComponent
import com.kurotkin.cameraqr.dagger.DaggerAppComponent

class CameraQR: Application() {

    companion object {
        lateinit var instance: CameraQR
        lateinit var  appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .build()
    }
}