package com.joris.presentation.presenter

import android.os.Bundle
import android.os.Parcelable
import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.business.usecase.MainNavigationUseCase
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize
import java.lang.ref.WeakReference

/**
 * Interface to communicate with the view
 */
interface SearchPresenter {

    // We could also use any sort of observables
    interface View {
        fun onViewStateChanged(viewState: ViewState?)
    }

    @Parcelize
    data class ViewState(
        val showLoading: Boolean,
        val showError: Boolean,
        val showList: Boolean,
        val content: List<ListContent>
    ) : Parcelable

    @Parcelize
    data class ListContent(val teamName: String?, val teamBadgeImageUrl: String?) : Parcelable

    fun onLeagueNameSubmitted(league: String?)
    fun onTeamSelected(teamName: String?)
    fun onSaveState(state: Bundle)
    fun onRestoreState(state: Bundle? = null)
    fun cleanup()
}

class SearchPresenterImpl(
    view: SearchPresenter.View,
    private val mainNavigationUseCase: MainNavigationUseCase,
    private val getTeamsFromLeagueUseCase: GetTeamsFromLeagueUseCase
) :
    SearchPresenter {

    private var viewState: SearchPresenter.ViewState? = null
    private var viewWeakRef: WeakReference<SearchPresenter.View>? = null
    private var job: Job? = null

    init {
        viewWeakRef = WeakReference(view)
    }

    override fun onLeagueNameSubmitted(league: String?) {
        stopAnyBackgroundCoroutine()

        updateViewState(
            showLoading = true,
            showError = false,
            showList = false,
            content = emptyList()
        )

        // GlobalScope is a potential source of leak
        // One of the reason I prefer ViewModel and its ViewModelScope
        job = GlobalScope.launch {
            val output =
                getTeamsFromLeagueUseCase.execute(GetTeamsFromLeagueUseCase.Input(league = league))

            withContext(Dispatchers.Main) {
                if (output.containsCriticalError()) {
                    updateViewState(
                        showLoading = false,
                        showError = true,
                        showList = false,
                        content = emptyList()
                    )
                } else {

                    // Transform list of Team into list of ListContent
                    val content = mutableListOf<SearchPresenter.ListContent>()
                    output.data.forEach {
                        content.add(SearchPresenter.ListContent(it.name, it.badgeImageUrl))
                    }

                    updateViewState(
                        showLoading = false,
                        showError = false,
                        showList = true,
                        content = content
                    )
                }
            }
        }
    }

    override fun onTeamSelected(teamName: String?) {
        mainNavigationUseCase.switchToTeamDetail(
            hashMapOf(
                Pair("teamName", teamName)
            )
        )
    }

    override fun onSaveState(state: Bundle) {
        return state.putParcelable("viewState", viewState)
    }

    override fun onRestoreState(state: Bundle?) {
        state?.let {
            val savedViewState = state.getParcelable<SearchPresenter.ViewState>("viewState")
            savedViewState?.let {
                updateViewState(
                    savedViewState.showLoading,
                    savedViewState.showError,
                    savedViewState.showList,
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
        content: List<SearchPresenter.ListContent>
    ) {
        // Update view state
        viewState = SearchPresenter.ViewState(showLoading, showError, showList, content)

        // Notify view with new state
        getView()?.onViewStateChanged(viewState)
    }

    private fun stopAnyBackgroundCoroutine() {
        job?.cancel()
    }

    private fun getView(): SearchPresenter.View? {
        return viewWeakRef?.get()
    }
}