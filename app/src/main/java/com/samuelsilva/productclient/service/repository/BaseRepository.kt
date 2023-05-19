package com.samuelsilva.productclient.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.Gson
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.service.constants.Constants
import com.samuelsilva.productclient.service.listener.APIListener
import com.samuelsilva.productclient.service.model.ServerErrorResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository(val context: Context) {

    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }

    private fun failResponseConverter(response: String): ServerErrorResponseModel {
        return Gson().fromJson(response, ServerErrorResponseModel::class.java)
    }

    fun <T> executeCall(call: Call<T>, listener: APIListener<T>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.code() == Constants.HTTP.SUCCESS ||
                    response.code() == Constants.HTTP.CREATED ||
                    response.code() == Constants.HTTP.NO_CONTENT) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else if (response.code() == Constants.HTTP.EXPIRED_LOGIN) {
                    listener.onFailure(context.getString(R.string.ERROR_EXPIRED_LOGIN), true)
                } else {
                    listener.onFailure(failResponseConverter(response.errorBody()!!.string()).detail, false)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED), false)
            }
        })
    }

    /**
     * Verifica se existe conexão com internet
     */
    fun isConnectionAvailable(): Boolean {
        var result = false

        // Gerenciador de conexão
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Verifica versão do sistema rodando a aplicação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNet = cm.activeNetwork ?: return false
            val netWorkCapabilities = cm.getNetworkCapabilities(activeNet) ?: return false
            result = when {
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            if (cm.activeNetworkInfo != null) {
                result = when (cm.activeNetworkInfo!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return result
    }
}