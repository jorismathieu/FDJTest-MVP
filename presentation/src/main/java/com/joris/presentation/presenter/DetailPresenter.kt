package com.joris.presentation.presenter

import android.os.Bundle
import android.os.Parcelable
import com.joris.business.usecase.GetTeamDetailsUseCase
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize
import java.lang.ref.WeakReference

interface DetailsPresenter {

    // We could also use any sort of observables
    interface View {
        fun onViewStateChanged(viewState: ViewState?)
    }

    @Parcelize
    data class ViewState(
        val showLoading: Boolean,
        val showError: Boolean,
        val showContent: Boolean,
        val content: Content?
    ) : Parcelable

    @Parcelize
    data class Content(
        val name: String?,
        val badgeImageUrl: String?,
        val bannerImageUrl: String?,
        val country: String?,
        val league: String?,
        val description: String?
    ) : Parcelable

    fun getTeamDetails(teamName: String?)
    fun onSaveState(state: Bundle)
    fun onRestoreState(state: Bundle? = null)
    fun cleanup()

    // We can add saveState and restoreState methods if needed
}

class DetailsPresenterImpl(
    view: DetailsPresenter.View,
    private val getTeamDetailsUseCase: GetTeamDetailsUseCase
) :
    DetailsPresenter {

    private var viewState: DetailsPresenter.ViewState? = null
    private var viewWeakRef: WeakReference<DetailsPresenter.View>? = null
    private var job: Job? = null

    init {
        viewWeakRef = WeakReference(view)
    }

    override fun getTeamDetails(teamName: String?) {
        stopAnyBackgroundCoroutine()

        updateViewState(
            showLoading = true,
            showError = false,
            showList = false
        )

        // GlobalScope is a potential source of leak
        // One of the reason I prefer ViewModel and its ViewModelScope
        job = GlobalScope.launch {
            val output =
                getTeamDetailsUseCase.execute(GetTeamDetailsUseCase.Input(teamName = teamName))

            withContext(Dispatchers.Main) {
                if (output.containsCriticalError()) {
                    updateViewState(
                        showLoading = false,
                        showError = true,
                        showList = false
                    )
                } else {
                    output.data?.let {
                        updateViewState(
                            showLoading = false,
                            showError = false,
                            showList = true,
                            content = DetailsPresenter.Content(
                                it.name,
                                it.badgeImageUrl,
                                it.bannerImageUrl,
                                it.country,
                                it.league,
                                it.description
                            )
                        )
                    } ?: run {
                        updateViewState(
                            showLoading = false,
                            showError = true,
                            showList = false
                        )
                    }
                }
            }
        }
    }

    override fun onSaveState(state: Bundle) {
        return state.putParcelable("viewState", viewState)
    }

    override fun onRestoreState(state: Bundle?) {
        state?.let {
            val savedViewState = state.getParcelable<DetailsPresenter.ViewState>("viewState")
            savedViewState?.let {
                updateViewState(
                    savedViewState.showLoading,
                    savedViewState.showError,
                    savedViewState.showContent,
                    savedViewState.content
                )
            }
        }
    }

    override fun cleanup() {
        stopAnyBackgroundCoroutine()
    }

    private fun updateViewState(
        showLoading: Boolean,
        showError: Boolean,
        showList: Boolean,
        content: DetailsPresenter.Content? = null
    ) {
        // Update view state
        viewState = DetailsPresenter.ViewState(showLoading, showError, showList, content)

        // Notify view with new state
        getView()?.onViewStateChanged(viewState)
    }

    private fun stopAnyBackgroundCoroutine() {
        job?.cancel()
    }

    private fun getView(): DetailsPresenter.View? {
        return viewWeakRef?.get()
    }

}