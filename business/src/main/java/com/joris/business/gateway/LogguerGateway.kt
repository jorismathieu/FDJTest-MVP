package com.joris.business.gateway


interface LogguerGateway {
    fun logDebug(message: String) {}
    fun logError(message: String, exception: Throwable? = null) {}
}