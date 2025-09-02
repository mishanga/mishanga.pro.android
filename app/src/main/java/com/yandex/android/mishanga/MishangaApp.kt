package com.yandex.android.mishanga

import android.app.Application
import android.util.Log
import com.yandex.mobile.ads.common.MobileAds

class MishangaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {
            Log.d("YangoAds", "MobileAds initialized")
        }
    }
}


