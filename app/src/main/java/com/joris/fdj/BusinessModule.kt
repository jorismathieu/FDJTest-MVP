package com.joris.fdj

import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.business.usecase.GetTeamsFromLeagueUseCaseImpl

/**
 * Could be replaced with hilt module (once stable), or Koin or any other dependency injection tool
 */
object BusinessModule {
    fun getGetTeamsFromLeagueUseCase(): GetTeamsFromLeagueUseCase {
        return GetTeamsFromLeagueUseCaseImpl(
            teamRepository =,
            logguerGateway =
        )
    }
}