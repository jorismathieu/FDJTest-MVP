package com.joris.presentation.presenter

import com.joris.business.entity.Team
import com.joris.business.usecase.GetTeamDetailsUseCase
import com.joris.business.usecase.base.CriticalErrorCode
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Interface to communicate with the view
 */
interface DetailsPresenter {
    interface View {
        fun onError()
        fun onSuccess(team: Team)
    }

    fun getTeamDetails(teamName: String?)

    fun cleanup()

    // We could add saveState and restoreState methods if needed
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
        launch {
            val output =
                getTeamDetailsUseCase.execute(GetTeamDetailsUseCase.Input(teamName = teamName))

            withContext(Dispatchers.Main) {
                if (output.containsCriticalError()) {
                    view.onError()
                } else {
                    output.data?.let {
                        view.onSuccess(it)
                    } ?: run {
                        view.onError()
                    }
                }
            }
        }
    }

    override fun cleanup() {
        cancelJob()
    }

    private fun cancelJob() {
        job.cancel()
    }

}