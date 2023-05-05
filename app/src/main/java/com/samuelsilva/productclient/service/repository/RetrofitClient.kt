package com.samuelsilva.productclient.service.repository

import com.samuelsilva.productclient.service.constants.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private lateinit var INSTANCE: Retrofit
        private var token: String? = ""

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()

                    val newRequest = if (token == null) {
                        request.newBuilder().build()
                    } else {
                        request.newBuilder()
                            .addHeader(Constants.HEADER.AUTHORIZATION, "${Constants.HEADER.BEARER} $token")
                            .build()
                    }

                    return chain.proceed(newRequest)
                }
            })

            if (!::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(Constants.PATH.IP)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHeaders(tokenValue: String?) {
            token = tokenValue
        }
    }
}