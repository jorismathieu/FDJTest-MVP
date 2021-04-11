package com.joris.business.gateway

// Platform dependant gateway to display logs
interface LogguerGateway {
    fun logDebug(message: String) {}
    fun logError(message: String, exception: Throwable? = null) {}
}