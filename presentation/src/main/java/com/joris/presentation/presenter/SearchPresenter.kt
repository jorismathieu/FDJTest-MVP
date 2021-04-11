package com.joris.presentation.presenter

import com.joris.business.entity.Screen
import com.joris.business.entity.Team
import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.business.usecase.MainNavigationUseCase
import com.joris.business.usecase.base.CriticalErrorCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.HashMap
import kotlin.coroutines.CoroutineContext

/**
 * Interface to communicate with the view
 */
interface SearchPresenter {
    interface View {
        fun onInternalErrorReceived()
        fun onDataAccessErrorReceived()
        fun onEmptyListReceived()
        fun onTeamListReceived(teams: List<Team>)
    }

    fun onLeagueNameSubmitted(league: String?)

    fun onTeamSelected(teamName: String?)

    fun cleanup()

    // We could add saveState and restoreState methods if needed
}

class SearchPresenterImpl(
    private val view: SearchPresenter.View,
    private val mainNavigationUseCase: MainNavigationUseCase,
    private val getTeamsFromLeagueUseCase: GetTeamsFromLeagueUseCase
) :
    SearchPresenter,
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun onLeagueNameSubmitted(league: String?) {
        cancelJob()

        launch {
            val output =
                getTeamsFromLeagueUseCase.execute(GetTeamsFromLeagueUseCase.Input(league = league))

            if (output.containsCriticalError()) {
                when (output.criticalError) {
                    CriticalErrorCode.DATA_ACCESS -> view.onDataAccessErrorReceived()
                    CriticalErrorCode.INTERNAL -> view.onInternalErrorReceived()
                }
            } else {
                if (output.data.isNotEmpty()) {
                    view.onTeamListReceived(output.data)
                } else {
                    view.onEmptyListReceived()
                }
            }
        }
    }

    override fun onTeamSelected(teamName: String?) {
        mainNavigationUseCase.switchToScreen(Screen.DETAIL, hashMapOf(
            Pair("teamName", teamName)
        ))
    }

    override fun cleanup() {
        cancelJob()
    }

    private fun cancelJob() {
        job.cancel()
    }

}