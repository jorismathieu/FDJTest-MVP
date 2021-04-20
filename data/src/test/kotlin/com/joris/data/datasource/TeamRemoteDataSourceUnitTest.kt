package com.joris.data.datasource

import com.joris.data.api.ApiConfiguration
import com.joris.data.mock.TeamWebServiceMock
import com.joris.data.mock.TeamWebServiceResponsesMock
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test


// To unit test remote data source object, we use a mock server (okhttp mockwebserver) and mock responses
class TeamRemoteDataSourceUnitTest {

    private val apiConfiguration by lazy {
        object : ApiConfiguration {
            override val API_BASE_URL = "http://localhost:37299/"
            override val CONNECT_TIMEOUT_SECONDS = 5L
            override val READ_TIMEOUT_SECONDS = 5L
            override val HEADER_DEFAULT_CACHE_MAX_AGE = "Cache-Control: max-age=0"
        }
    }

    @Test
    fun test_searchAllTeams() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_ALL_TEAMS_MOCK)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            val teams = teamRemoteDataSource.getTeamsFromLeague("")

            assert(teams != null)
            assert(teams?.isNotEmpty() ?: false)
            assert(teams?.size == 2)

            Assert.assertEquals("team1", teams?.get(0)?.name)
            Assert.assertEquals("badge1", teams?.get(0)?.badgeImageUrl)
            Assert.assertEquals("banner1", teams?.get(0)?.bannerImageUrl)
            Assert.assertEquals("country1", teams?.get(0)?.country)
            Assert.assertEquals("league1", teams?.get(0)?.league)
            Assert.assertEquals("description1", teams?.get(0)?.description)

            Assert.assertEquals("team2", teams?.get(1)?.name)
            Assert.assertEquals("badge2", teams?.get(1)?.badgeImageUrl)
            Assert.assertEquals("banner2", teams?.get(1)?.bannerImageUrl)
            Assert.assertEquals("country2", teams?.get(1)?.country)
            Assert.assertEquals("league2", teams?.get(1)?.league)
            Assert.assertEquals("description2", teams?.get(1)?.description)

            teamWebServiceMock.shutDown()
        }
    }

    @Test
    fun test_searchAllTeams_empty() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_ALL_TEAMS_EMPTY)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            val teams = teamRemoteDataSource.getTeamsFromLeague("")

            assert(teams != null)
            assert(teams?.isEmpty() ?: false)

            teamWebServiceMock.shutDown()
        }
    }

    @Test
    fun test_searchAllTeams_parsingError() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_ALL_TEAMS_PARSING_ERROR)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            val teams = teamRemoteDataSource.getTeamsFromLeague("")

            assert(teams == null)

            teamWebServiceMock.shutDown()
        }
    }

    @Test
    fun test_searchAllTeams_error() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_ALL_TEAMS_ERROR)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            try {
                teamRemoteDataSource.getTeamsFromLeague("")
                assert(false) // Call should fail and throw exception
            } catch (e: Throwable) {
                assert(true)
            }

            teamWebServiceMock.shutDown()
        }
    }

    @Test
    fun test_searchTeam() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_TEAMS_MOCK)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            val team = teamRemoteDataSource.getTeamDetails("")

            assert(team != null)

            Assert.assertEquals("team1", team?.name)
            Assert.assertEquals("badge1", team?.badgeImageUrl)
            Assert.assertEquals("banner1", team?.bannerImageUrl)
            Assert.assertEquals("country1", team?.country)
            Assert.assertEquals("league1", team?.league)
            Assert.assertEquals("description1", team?.description)

            teamWebServiceMock.shutDown()
        }
    }

    @Test
    fun test_searchTeam_empty() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_TEAMS_EMPTY)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            try {
                teamRemoteDataSource.getTeamDetails("")
                assert(false) // Call should fail and throw exception
            } catch (e: Throwable) {
                assert(true)
            }

            teamWebServiceMock.shutDown()
        }
    }

    @Test
    fun test_searchTeam_parsingError() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_TEAMS_PARSING_ERROR)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            val team = teamRemoteDataSource.getTeamDetails("")

            assert(team == null)

            teamWebServiceMock.shutDown()
        }
    }

    @Test
    fun test_searchTeam_error() {
        runBlocking {
            val teamWebServiceMock =
                TeamWebServiceMock("localhost", 37299, TeamWebServiceResponsesMock.SEARCH_TEAMS_ERROR)
            teamWebServiceMock.start()

            val teamRemoteDataSource = TeamRemoteDataSourceImpl(apiConfiguration)
            try {
                teamRemoteDataSource.getTeamDetails("")
                assert(false) // Call should fail and throw exception
            } catch (e: Throwable) {
                assert(true)
            }

            teamWebServiceMock.shutDown()
        }
    }
}