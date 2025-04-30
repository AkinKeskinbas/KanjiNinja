package com.keak.kanjininja.network

import com.keak.kanjininja.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor():Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val initial = chain.request()
        val builder = initial.run { newBuilder() }
        val request = commonHeaders(builder).method(initial.method, initial.body).build()
        return chain.proceed(request)
    }
    private fun commonHeaders(builder: Request.Builder): Request.Builder {
        builder.header("x-rapidapi-host", "kanjialive-api.p.rapidapi.com")
        builder.header("x-rapidapi-key", BuildConfig.RapidApiKey)
        return builder
    }
}