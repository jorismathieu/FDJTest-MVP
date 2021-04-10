package com.joris.business

import com.joris.business.gateway.LogguerGateway
import com.joris.business.repository.TeamRepository
import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.business.usecase.GetTeamsFromLeagueUseCaseImpl


object Business {
    fun getGetTeamsFromLeagueUseCase(
        teamRepository: TeamRepository,
        logguerGateway: LogguerGateway
    ): GetTeamsFromLeagueUseCase {
        return GetTeamsFromLeagueUseCaseImpl(
            teamRepository = teamRepository,
            logguerGateway = logguerGateway)
    }
}