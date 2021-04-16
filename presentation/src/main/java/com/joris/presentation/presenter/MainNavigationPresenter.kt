package com.joris.presentation.presenter

import android.os.Bundle
import android.os.Parcelable
import com.joris.business.entity.Screen
import com.joris.business.usecase.MainNavigationUseCase
import kotlinx.parcelize.Parcelize
import java.lang.ref.WeakReference

interface MainNavigationPresenter {
    interface View {
        fun onViewStateChanged(viewState: ViewState?)
    }

    @Parcelize
    data class ViewState(
        val screen: Screen?,
        val args: Bundle?,
        val goToPreviousScreen: Boolean?
    ) : Parcelable

    fun subscribeToScreenChanges()
    fun unsubscribeToScreenChanges()
    fun onSaveState(state: Bundle)
    fun onRestoreState(state: Bundle? = null)
    fun onBackPressed()
}

class MainNavigationPresenterImpl(
    view: MainNavigationPresenter.View,
    private val mainNavigationUseCase: MainNavigationUseCase,
) : MainNavigationPresenter {

    private var viewWeakRef: WeakReference<MainNavigationPresenter.View>? = null
    private var viewState: MainNavigationPresenter.ViewState? = null

    init {
        viewWeakRef = WeakReference(view)
    }

    private val screenChangeObserver: MainNavigationUseCase.ScreenChangeObserver by lazy {
        object : MainNavigationUseCase.ScreenChangeObserver {
            override fun onScreenChange(screen: Screen, params: Map<String, String?>?) {
                val args = params?.let { map ->
                    Bundle().apply {
                        for (key in map.keys) {
                            putString(key, map[key])
                        }
                    }
                }
                updateViewState(screen = screen, args = args)
            }

            override fun onGoToPreviousScreen() {
                updateViewState(goToPreviousScreen = true)
            }

        }
    }

    override fun subscribeToScreenChanges() {
        mainNavigationUseCase.subscribe(screenChangeObserver)
    }

    override fun unsubscribeToScreenChanges() {
        mainNavigationUseCase.unsubscribe()
    }

    override fun onSaveState(state: Bundle) {
        return state.putParcelable("viewState", viewState)
    }

    override fun onRestoreState(state: Bundle?) {
        state?.let {
            val savedViewState = state.getParcelable<MainNavigationPresenter.ViewState>("viewState")
            savedViewState?.let {
                updateViewState(
                    screen = savedViewState.screen,
                    args = savedViewState.args
                )
            }
        } ?: run {
            updateViewState(
                screen = Screen.SEARCH
            )
        }
    }

    override fun onBackPressed() {
        mainNavigationUseCase.onPressBack()
    }

    private fun updateViewState(
        screen: Screen? = null,
        args: Bundle? = null,
        goToPreviousScreen: Boolean? = null
    ) {
        // Update view state
        viewState = MainNavigationPresenter.ViewState(screen, args, goToPreviousScreen)

        // Notify view with new state
        viewWeakRef?.get()?.onViewStateChanged(viewState)
    }

}