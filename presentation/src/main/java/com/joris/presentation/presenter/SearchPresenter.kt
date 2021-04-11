package com.joris.presentation.presenter

import com.joris.business.entity.Screen
import com.joris.business.entity.Team
import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.business.usecase.MainNavigationUseCase
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

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
    view: SearchPresenter.View,
    private val mainNavigationUseCase: MainNavigationUseCase,
    private val getTeamsFromLeagueUseCase: GetTeamsFromLeagueUseCase
) :
    SearchPresenter {

    private var viewWeakRef: WeakReference<SearchPresenter.View>? = null
    private var job: Job? = null

    init {
        viewWeakRef = WeakReference(view)
    }

    override fun onLeagueNameSubmitted(league: String?) {
        stopAnyBackgroundCoroutine()

        getView()?.onShowProgressBar()
        getView()?.onHideError()
        getView()?.onHideContent()

        // GlobalScope is a potential source of leak
        // One of the reason I prefer ViewModel and its ViewModelScope
        job = GlobalScope.launch {
            val output =
                getTeamsFromLeagueUseCase.execute(GetTeamsFromLeagueUseCase.Input(league = league))

            withContext(Dispatchers.Main) {
                getView()?.onHideProgressBar()
                if (output.containsCriticalError()) {
                    getView()?.onShowError()
                    getView()?.onHideContent()
                } else {
                    getView()?.onShowContent(output.data)
                    getView()?.onHideError()
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
        stopAnyBackgroundCoroutine()
    }

    private fun stopAnyBackgroundCoroutine() {
        job?.cancel()
    }

    private fun getView(): SearchPresenter.View? {
        return viewWeakRef?.get()
    }
}