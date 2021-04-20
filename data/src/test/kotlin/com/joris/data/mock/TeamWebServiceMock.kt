package com.joris.data.mock

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.InetAddress

class TeamWebServiceMock(
    private val address: String,
    private val port: Int,
    private val response: MockResponse
) {

    // Create a MockWebServer. These are lean enough that you can create a new
    // instance for every unit test.
    private val server = MockWebServer()

    init {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                println("Received request: method=${request.method} path=${request.path}")
                return response
            }
        }
    }


    fun start() {
        println("MockWebService starting...")
        try {
            // Start the server.
            server.start(InetAddress.getByName(address), port)
            // Ask the server for its URL. You'll need this to make HTTP requests.
            val baseUrl = server.url("")
            println("MockWebService started with baseUrl=$baseUrl")
        } catch (t: Throwable) {
            error("MockWebService failed to start: $t")
        }
    }

    fun shutDown() {
        // Shut down the server. Instances cannot be reused.
        println("MockWebService shutting down...")
        try {
            server.shutdown()
            println("MockWebService has been shut down.")
        } catch (t: Throwable) {
            error("MockWebService failed to shut down: $t")
        }
    }

}