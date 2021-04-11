package com.joris.business.usecase

import com.joris.business.entity.Team
import com.joris.business.gateway.LogguerGateway
import com.joris.business.repository.TeamRepository
import com.joris.business.usecase.base.CriticalErrorCode
import com.joris.business.usecase.base.SuspendUseCase
import com.joris.business.usecase.base.UseCaseOutput

interface GetTeamDetailsUseCase :
    SuspendUseCase<GetTeamDetailsUseCase.Input, GetTeamDetailsUseCase.Output> {

    class Input(val teamName: String?)

    class Output(
        override val criticalError: CriticalErrorCode? = null,
        override val data: Team? = null
    ) : UseCaseOutput<Team?>()

    override suspend fun execute(input: Input): Output
}

class GetTeamDetailsUseCaseImpl(
    private val teamRepository: TeamRepository,
    private val logguerGateway: LogguerGateway
) : GetTeamDetailsUseCase {

    override suspend fun execute(input: GetTeamDetailsUseCase.Input): GetTeamDetailsUseCase.Output {
        try {
            input.teamName?.let { teamName ->
                val team = teamRepository.getTeamDetails(teamName = teamName)
                team?.let {
                    return GetTeamDetailsUseCase.Output(data = it)
                } ?: run {
                    return GetTeamDetailsUseCase.Output(criticalError = CriticalErrorCode.DATA_ACCESS)
                }
            }
            return GetTeamDetailsUseCase.Output(criticalError = CriticalErrorCode.INTERNAL)
        } catch (throwable: Throwable) {
            logguerGateway.logError(
                "Use case error while getting team detail for team : ${input.teamName}",
                throwable
            )
        }
        return GetTeamDetailsUseCase.Output(criticalError = CriticalErrorCode.DATA_ACCESS)
    }

}
