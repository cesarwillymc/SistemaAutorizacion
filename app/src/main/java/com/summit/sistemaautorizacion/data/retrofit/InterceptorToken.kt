package com.summit.sistemaautorizacion.data.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.summit.sistemaautorizacion.app.MyApp.Companion.getContextApp
import com.summit.sistemaautorizacion.common.Constants
import com.summit.sistemaautorizacion.common.SharedPreferencsManager.Companion.getSomeStringValue
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class InterceptorToken: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request =
            chain.request().newBuilder().addHeader("Authorization", getSomeStringValue(Constants.PREF_TOKEN)!!).build()
        return chain.proceed(request)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun isInternetAvailable(): Boolean {
        var result= false
        val connectivityManager= getContextApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result= when {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN)->true
                    else->false
                }
            }
        }
        return result
    }
}