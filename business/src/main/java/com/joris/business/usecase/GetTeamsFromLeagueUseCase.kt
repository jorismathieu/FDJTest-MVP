package com.joris.business.usecase

import com.joris.business.entity.Team
import com.joris.business.gateway.LogguerGateway
import com.joris.business.repository.TeamRepository
import com.joris.business.usecase.base.CriticalErrorCode
import com.joris.business.usecase.base.SuspendUseCase
import com.joris.business.usecase.base.SuspendUseCaseOutput

interface GetTeamsFromLeagueUseCase :
    SuspendUseCase<GetTeamsFromLeagueUseCase.Input, GetTeamsFromLeagueUseCase.Output> {

    class Input(val league: String?)

    class Output(
        override val criticalError: CriticalErrorCode? = null,
        override val data: List<Team> = emptyList()
    ) : SuspendUseCaseOutput<List<Team>?>()

    override suspend fun execute(input: Input): Output
}

class GetTeamsFromLeagueUseCaseImpl(
    private val teamRepository: TeamRepository,
    private val logguerGateway: LogguerGateway
) : GetTeamsFromLeagueUseCase {

    override suspend fun execute(input: GetTeamsFromLeagueUseCase.Input): GetTeamsFromLeagueUseCase.Output {
        try {
            input.league?.let { league ->
                val teams = teamRepository.getTeamsFromLeague(league = league)
                teams?.let {
                    return GetTeamsFromLeagueUseCase.Output(data = it)
                } ?: run {
                    return GetTeamsFromLeagueUseCase.Output(criticalError = CriticalErrorCode.DATA_ACCESS)
                }
            }
            return GetTeamsFromLeagueUseCase.Output(criticalError = CriticalErrorCode.INTERNAL)
        } catch (throwable: Throwable) {
            logguerGateway.logError(
                "Use case error while getting teams for league : ${input.league}",
                throwable
            )
        }
        return GetTeamsFromLeagueUseCase.Output(criticalError = CriticalErrorCode.DATA_ACCESS)
    }

}
