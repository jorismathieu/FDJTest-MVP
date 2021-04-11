package com.joris.business.usecase

import com.joris.business.entity.Team
import com.joris.business.gateway.LogguerGateway
import com.joris.business.mock.getTeamRepositoryMock
import com.joris.business.usecase.base.CriticalErrorCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetTeamsFromLeagueUseCaseUnitTest {

    @Test
    fun test_dataAccessErrorWhenExceptionIsThrown() {
        runBlocking {
            // Call
            val output = GetTeamsFromLeagueUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamsFromLeagueLambda = {
                        // Repository throws an exception
                        throw Exception()
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamsFromLeagueUseCase.Input(
                        league = "Test"
                    )
                )

            // Check errors
            Assert.assertEquals(true, output.containsCriticalError())
            Assert.assertEquals(CriticalErrorCode.DATA_ACCESS, output.criticalError)
        }
    }

    @Test
    fun test_dataAccessErrorWhenListIsNull() {
        runBlocking {
            // Call
            val output = GetTeamsFromLeagueUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamsFromLeagueLambda = {
                        // Repository returns null
                        null
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamsFromLeagueUseCase.Input(
                        "Test"
                    )
                )

            // Check errors
            Assert.assertEquals(true, output.containsCriticalError())
            Assert.assertEquals(CriticalErrorCode.DATA_ACCESS, output.criticalError)
        }
    }

    @Test
    fun test_internalErrorWhenLeagueIsNull() {
        runBlocking {
            // Call
            val output = GetTeamsFromLeagueUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamsFromLeagueLambda = {
                        throw Exception()
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamsFromLeagueUseCase.Input(
                        // We send null as league = error
                        league = null
                    )
                )

            // Check errors
            Assert.assertEquals(true, output.containsCriticalError())
            Assert.assertEquals(CriticalErrorCode.INTERNAL, output.criticalError)
        }
    }

    @Test
    fun test_internalErrorWhenLeagueIsEmpty() {
        runBlocking {
            // Call
            val output = GetTeamsFromLeagueUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamsFromLeagueLambda = {
                        throw Exception()
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamsFromLeagueUseCase.Input(
                        // We send null as league = error
                        league = ""
                    )
                )

            // Check errors
            Assert.assertEquals(true, output.containsCriticalError())
            Assert.assertEquals(CriticalErrorCode.INTERNAL, output.criticalError)
        }
    }

    @Test
    fun test_success() {
        runBlocking {
            // Call
            val output = GetTeamsFromLeagueUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamsFromLeagueLambda = {
                        listOf(object : Team {
                            override val name = "team1"
                            override val badgeImageUrl = "badge1"
                            override val bannerImageUrl = "banner1"
                            override val country = "country1"
                            override val league = "league1"
                            override val description = "description1"

                        }, object : Team {
                            override val name = "team2"
                            override val badgeImageUrl = "badge2"
                            override val bannerImageUrl = "banner2"
                            override val country = "country2"
                            override val league = "league2"
                            override val description = "description2"

                        })
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamsFromLeagueUseCase.Input(
                        league = "Test"
                    )
                )

            // Check errors
            Assert.assertEquals(false, output.containsCriticalError())

            // Check data
            Assert.assertEquals(2, output.data.size)

            Assert.assertEquals("team1", output.data[0].name)
            Assert.assertEquals("badge1", output.data[0].badgeImageUrl)
            Assert.assertEquals("banner1", output.data[0].bannerImageUrl)
            Assert.assertEquals("country1", output.data[0].country)
            Assert.assertEquals("league1", output.data[0].league)
            Assert.assertEquals("description1", output.data[0].description)

            Assert.assertEquals("team2", output.data[1].name)
            Assert.assertEquals("badge2", output.data[1].badgeImageUrl)
            Assert.assertEquals("banner2", output.data[1].bannerImageUrl)
            Assert.assertEquals("country2", output.data[1].country)
            Assert.assertEquals("league2", output.data[1].league)
            Assert.assertEquals("description2", output.data[1].description)

        }
    }

}
