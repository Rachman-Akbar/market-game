package com.example.luminasdgs

import android.app.Application
import com.example.luminasdgs.data.remote.ApiClient

/**
 * Application subclass that eagerly initializes the networking layer so that
 * repositories can be used from any screen (including games finished before
 * the profile screen is ever visited).
 */
class SDGSApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClient.initialize(this)
    }
}
