package com.yandex.android.mishanga.data

import android.content.Context
import android.content.SharedPreferences

data class AdSettings(
    val aimBannerId: String = "",
    val regionId: String = "",
    val uuid: String = "",
    val adUnitId: String = "",
)

class SettingsStore private constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun read(): AdSettings = AdSettings(
        aimBannerId = prefs.getString(KEY_AIM_BANNER_ID, "") ?: "",
        regionId = prefs.getString(KEY_REGION_ID, "") ?: "",
        uuid = prefs.getString(KEY_UUID, "") ?: "",
        adUnitId = prefs.getString(KEY_AD_UNIT_ID, "") ?: "",
    )

    fun write(value: AdSettings) {
        prefs.edit()
            .putString(KEY_AIM_BANNER_ID, value.aimBannerId)
            .putString(KEY_REGION_ID, value.regionId)
            .putString(KEY_UUID, value.uuid)
            .putString(KEY_AD_UNIT_ID, value.adUnitId)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "yango_ads_settings"
        private const val KEY_AIM_BANNER_ID = "aim_banner_id"
        private const val KEY_REGION_ID = "region_id"
        private const val KEY_UUID = "uuid"
        private const val KEY_AD_UNIT_ID = "ad_unit_id"

        @Volatile
        private var instance: SettingsStore? = null

        fun get(context: Context): SettingsStore =
            instance ?: synchronized(this) {
                instance ?: SettingsStore(context.applicationContext).also { instance = it }
            }
    }
}



