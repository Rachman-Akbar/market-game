package com.example.marketgame

import android.app.Application
import com.example.marketgame.data.remote.ApiClient

/**
 * Application subclass that eagerly initializes the networking layer so that
 * repositories can be used from any screen (including games finished before
 * the profile screen is ever visited).
 */
class MarketGameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClient.initialize(this)
    }
}
