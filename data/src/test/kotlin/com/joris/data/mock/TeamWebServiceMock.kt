package com.joris.data.mock

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.InetAddress

class TeamWebServiceMock(
    private val address: String,
    private val port: Int,
    private val strategy: Strategy
) {

    enum class Strategy {
        MOCK,
        EMPTY,
        PARSING_ERROR,
        ERROR
    }

    // Create a MockWebServer. These are lean enough that you can create a new
    // instance for every unit test.
    private val server = MockWebServer()

    init {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                println("Received request: method=${request.method} path=${request.path}")
                request.path?.let {
                    when {
                        it.contains("search_all_teams") -> {
                            return when (strategy) {
                                Strategy.MOCK -> MockResponse().setBody(
                                    TeamWebServiceResponsesMock.SEARCH_ALL_TEAMS_MOCK
                                )
                                Strategy.EMPTY -> MockResponse().setBody(
                                    TeamWebServiceResponsesMock.SEARCH_ALL_TEAMS_EMPTY
                                )
                                Strategy.PARSING_ERROR -> MockResponse().setBody(
                                    TeamWebServiceResponsesMock.SEARCH_ALL_TEAMS_PARSING_ERROR
                                )
                                Strategy.ERROR -> MockResponse().setResponseCode(500)
                            }
                        }
                        it.contains("searchteams") -> {
                            return when (strategy) {
                                Strategy.MOCK -> MockResponse().setBody(
                                    TeamWebServiceResponsesMock.SEARCH_TEAMS_MOCK
                                )
                                Strategy.EMPTY -> MockResponse().setBody(
                                    TeamWebServiceResponsesMock.SEARCH_TEAMS_EMPTY
                                )
                                Strategy.PARSING_ERROR -> MockResponse().setBody(
                                    TeamWebServiceResponsesMock.SEARCH_TEAMS_PARSING_ERROR
                                )
                                Strategy.ERROR -> MockResponse()
                            }
                        }
                        else -> {
                        }
                    }
                }

                return MockResponse().setBody("Path ${request.path} not supported!")
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