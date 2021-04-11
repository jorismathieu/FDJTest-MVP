package com.joris.business.usecase

import com.joris.business.entity.Team
import com.joris.business.gateway.LogguerGateway
import com.joris.business.mock.getTeamRepositoryMock
import com.joris.business.usecase.base.CriticalErrorCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetTeamDetailsUseCaseUnitTest {

    @Test
    fun test_dataAccessErrorWhenExceptionIsThrown() {
        runBlocking {
            // Call
            val output = GetTeamDetailsUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamDetailLambda = {
                        // Repository throws an exception
                        throw Exception()
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamDetailsUseCase.Input(
                        teamName = ""
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
            val output = GetTeamDetailsUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamDetailLambda = {
                        // Repository returns null
                        null
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamDetailsUseCase.Input(
                        ""
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
            val output = GetTeamDetailsUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamDetailLambda = {
                        throw Exception()
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamDetailsUseCase.Input(
                        // We send null as league = error
                        teamName = null
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
            val output = GetTeamDetailsUseCaseImpl(
                getTeamRepositoryMock(
                    getTeamDetailLambda = {
                        object : Team {
                            override val name = "team1"
                            override val badgeImageUrl = "badge1"
                            override val bannerImageUrl = "banner1"
                            override val country = "country1"
                            override val league = "league1"
                            override val description = "description1"

                        }
                    }
                ),
                object : LogguerGateway {})
                .execute(
                    GetTeamDetailsUseCase.Input(
                        teamName = ""
                    )
                )

            // Check errors
            Assert.assertEquals(false, output.containsCriticalError())

            // Check data
            Assert.assertTrue(output.data != null)
            Assert.assertEquals("team1", output.data?.name)
            Assert.assertEquals("badge1", output.data?.badgeImageUrl)
            Assert.assertEquals("banner1", output.data?.bannerImageUrl)
            Assert.assertEquals("country1", output.data?.country)
            Assert.assertEquals("league1", output.data?.league)
            Assert.assertEquals("description1", output.data?.description)

        }
    }

}
