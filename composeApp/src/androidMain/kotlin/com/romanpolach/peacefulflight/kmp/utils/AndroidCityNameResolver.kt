package com.romanpolach.peacefulflight.kmp.utils

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class AndroidCityNameResolver(context: Context) : CityNameResolver {

    private val geocoder = Geocoder(context, Locale.getDefault())

    override suspend fun getCityName(latitude: Double, longitude: Double): String? =
        withContext(Dispatchers.IO) {
            runCatching {
                val address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
                address?.locality
                    ?: address?.subAdminArea
                    ?: address?.adminArea
            }.getOrNull()
        }
}
