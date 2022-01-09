package com.imdmp.paperless

import android.app.Application
import timber.log.Timber

class PaperlessApplication : Application(){


    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}