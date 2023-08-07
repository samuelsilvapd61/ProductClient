package com.samuelsilva.productclient.service.constants

/**
 * Constantes usadas na aplicação
 */
class Constants private constructor() {

    // SharedPreferences
    object SHARED {
        const val TOKEN_KEY = "tokenkey"
        const val IP_KEY = "ipkey"
        const val PORT_KEY = "portkey"
    }

    // Requisições API
    object HEADER {
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
    }

    object PATH {
        var PATH = "http://10.0.0.0:8888"

        fun setPath(ip: String, port: String) {
            PATH = "http://${ip}:${port}/"
        }
    }

    object HTTP {
        const val SUCCESS = 200
        const val CREATED = 201
        const val NO_CONTENT = 204
        const val EXPIRED_LOGIN = 403
    }

}