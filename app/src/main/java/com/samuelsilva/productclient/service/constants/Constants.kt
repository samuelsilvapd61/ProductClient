package com.samuelsilva.productclient.service.constants

/**
 * Constantes usadas na aplicação
 */
class Constants private constructor() {

    // SharedPreferences
    object SHARED {
        const val TOKEN_KEY = "tokenkey"
    }

    // Requisições API
    object HEADER {
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
    }

    object PATH {
        const val IP = "http://192.168.1.80:8888/"
    }

    object HTTP {
        const val SUCCESS = 200
        const val EXPIRED_LOGIN = 403
    }

    object BUNDLE {
        const val TASKID = "taskid"
        const val TASKFILTER = "taskfilter"
    }

    // Filtro de tarefas
    object FILTER {
        const val ALL = 0
        const val NEXT = 1
        const val EXPIRED = 2
    }

}