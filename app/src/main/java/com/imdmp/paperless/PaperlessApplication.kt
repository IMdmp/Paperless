package com.imdmp.paperless

import android.app.Application
import timber.log.Timber
import com.facebook.flipper.plugins.inspector.DescriptorMapping

import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin

import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.soloader.SoLoader

class PaperlessApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        SoLoader.init(this, false);

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin( DatabasesFlipperPlugin(this));
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.start()
        }
    }
}