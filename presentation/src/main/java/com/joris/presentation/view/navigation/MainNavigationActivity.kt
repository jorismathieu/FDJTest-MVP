package com.joris.presentation.view.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.joris.business.entity.Screen
import com.joris.fdj.R
import com.joris.presentation.presenter.MainNavigationPresenter
import com.joris.presentation.view.screens.DetailFragment
import com.joris.presentation.view.screens.SearchFragment
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

/**
 * This is the unique activity of the app, its unique job is routing to the requested screen
 */
class MainNavigationActivity : AppCompatActivity(), MainNavigationPresenter.View {

    private val mainNavigationPresenter: MainNavigationPresenter by inject(
        clazz = MainNavigationPresenter::class.java,
        parameters = { parametersOf(this@MainNavigationActivity) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainNavigationPresenter.subscribeToScreenChanges()
        mainNavigationPresenter.onRestoreState(savedInstanceState)
    }

    override fun onViewStateChanged(viewState: MainNavigationPresenter.ViewState?) {
        if (!supportFragmentManager.isStateSaved) {
            if (viewState?.goToPreviousScreen == true) {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            } else {
                when (viewState?.screen) {
                    Screen.SEARCH -> if (supportFragmentManager.findFragmentByTag("search") == null) {
                        supportFragmentManager.commit {
                            add<SearchFragment>(R.id.fragment_container, tag = "search")
                        }
                    }
                    Screen.DETAIL -> if (supportFragmentManager.findFragmentByTag("detail") == null) {
                        supportFragmentManager.commit {
                            add<DetailFragment>(
                                R.id.fragment_container,
                                args = viewState.args,
                                tag = "detail"
                            )
                            addToBackStack(null)
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        mainNavigationPresenter.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mainNavigationPresenter.onSaveState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        mainNavigationPresenter.unsubscribeToScreenChanges()
    }
}