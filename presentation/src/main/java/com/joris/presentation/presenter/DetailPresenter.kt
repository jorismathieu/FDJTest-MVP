package com.joris.presentation.presenter

import com.joris.business.entity.Team
import com.joris.business.usecase.GetTeamDetailsUseCase
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

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
    view: DetailsPresenter.View,
    private val getTeamDetailsUseCase: GetTeamDetailsUseCase
) :
    DetailsPresenter {

    private var viewWeakRef: WeakReference<DetailsPresenter.View>? = null
    private var job: Job? = null

    init {
        viewWeakRef = WeakReference(view)
    }

    override fun getTeamDetails(teamName: String?) {
        stopAnyBackgroundCoroutine()

        getView()?.onShowProgressBar()
        getView()?.onHideError()
        getView()?.onHideContent()

        // GlobalScope is a potential source of leak
        // One of the reason I prefer ViewModel and its ViewModelScope
        job = GlobalScope.launch {
            val output =
                getTeamDetailsUseCase.execute(GetTeamDetailsUseCase.Input(teamName = teamName))

            withContext(Dispatchers.Main) {
                getView()?.onHideProgressBar()
                if (output.containsCriticalError()) {
                    getView()?.onShowError()
                    getView()?.onHideContent()
                } else {
                    output.data?.let {
                        getView()?.onShowContent(it)
                        getView()?.onHideError()
                    } ?: run {
                        getView()?.onShowError()
                        getView()?.onHideContent()
                    }
                }
            }
        }
    }

    override fun cleanup() {
        stopAnyBackgroundCoroutine()
    }

    private fun stopAnyBackgroundCoroutine() {
        job?.cancel()
    }

    private fun getView(): DetailsPresenter.View? {
        return viewWeakRef?.get()
    }

}