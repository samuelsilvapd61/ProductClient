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

        // Configuração dinâmica do baseUrl com IP e porta
        fun configureBaseUrl(ip: String, port: String) {
            synchronized(RetrofitClient::class) {
                val baseUrl = "http://$ip:$port/" // Use https:// se for uma conexão segura
                INSTANCE = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(createHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }

        private fun createHttpClient(): OkHttpClient {
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
            return httpClient.build()
        }

        fun <T> getService(serviceClass: Class<T>): T {
            if (!::INSTANCE.isInitialized) {
                throw IllegalStateException("RetrofitClient not initialized. Call configureBaseUrl() first.")
            }
            return INSTANCE.create(serviceClass)
        }

        fun addHeaders(tokenValue: String?) {
            token = tokenValue
        }
    }
}