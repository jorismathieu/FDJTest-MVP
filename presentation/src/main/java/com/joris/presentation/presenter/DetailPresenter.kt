package com.joris.presentation.presenter

import com.joris.business.entity.Team
import com.joris.business.usecase.GetTeamDetailsUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

interface DetailsPresenter {

    // We could also use any sort of observables
    interface View {
        fun onShowError()
        fun onHideError()
        fun onShowProgressBar()
        fun onHideProgressBar()
        fun onShowContent(team: Team)
        fun onHideContent()
    }

    fun getTeamDetails(teamName: String?)

    fun cleanup()

    // We can add saveState and restoreState methods if needed
}

class DetailsPresenterImpl(
    private val view: DetailsPresenter.View,
    private val getTeamDetailsUseCase: GetTeamDetailsUseCase
) :
    DetailsPresenter,
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    override fun getTeamDetails(teamName: String?) {
        view.onShowProgressBar()
        view.onHideError()
        view.onHideContent()
        launch {
            val output =
                getTeamDetailsUseCase.execute(GetTeamDetailsUseCase.Input(teamName = teamName))

            withContext(Dispatchers.Main) {
                view.onHideProgressBar()
                if (output.containsCriticalError()) {
                    view.onShowError()
                    view.onHideContent()
                } else {
                    output.data?.let {
                        view.onShowContent(it)
                        view.onHideError()
                    } ?: run {
                        view.onShowError()
                        view.onHideContent()
                    }
                }
            }
        }
    }

    override fun cleanup() {
        job.cancel()
    }

}