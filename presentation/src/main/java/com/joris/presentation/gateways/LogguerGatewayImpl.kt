package com.joris.presentation.gateways

import android.util.Log
import com.joris.business.gateway.LogguerGateway

class LogguerGatewayImpl: LogguerGateway {

    private val logTag = LogguerGatewayImpl::class.java.simpleName

    override fun logDebug(message: String) {
        Log.d(logTag, message)
    }

    override fun logError(message: String, exception: Throwable?) {
        Log.e(logTag, message, exception)
    }
}
