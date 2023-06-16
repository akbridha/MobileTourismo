package com.example.tourismo.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(token: String) :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}