package com.joris.presentation.presenter

import com.joris.business.entity.Screen
import com.joris.business.entity.Team
import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.business.usecase.MainNavigationUseCase
import com.joris.business.usecase.base.CriticalErrorCode
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Interface to communicate with the view
 */
interface SearchPresenter {

    // We could also use any sort of observables
    interface View {
        fun onShowError()
        fun onHideError()
        fun onShowProgressBar()
        fun onHideProgressBar()
        fun onShowContent(teams: List<Team>)
        fun onHideContent()
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
        view.onShowProgressBar()
        view.onHideError()
        view.onHideContent()
        launch {
            val output =
                getTeamsFromLeagueUseCase.execute(GetTeamsFromLeagueUseCase.Input(league = league))

            withContext(Dispatchers.Main) {
                view.onHideProgressBar()
                if (output.containsCriticalError()) {
                    view.onShowError()
                    view.onHideContent()
                } else {
                    view.onShowContent(output.data)
                    view.onHideError()
                }
            }
        }
    }

    override fun onTeamSelected(teamName: String?) {
        mainNavigationUseCase.switchToScreen(
            Screen.DETAIL, hashMapOf(
                Pair("teamName", teamName)
            )
        )
    }

    override fun cleanup() {
        job.cancel()
    }
}