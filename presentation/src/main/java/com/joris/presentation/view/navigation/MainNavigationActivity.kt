package com.joris.presentation.view.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.joris.business.entity.Screen
import com.joris.fdj.R
import com.joris.presentation.presenter.MainNavigationPresenter
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class MainNavigationActivity : AppCompatActivity(), MainNavigationPresenter.View {

    private val mainNavigationPresenter: MainNavigationPresenter by inject(
        clazz = MainNavigationPresenter::class.java,
        parameters = { parametersOf(this@MainNavigationActivity) })

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        mainNavigationPresenter.subscribeToScreenChanges()
    }

    override fun onDestroy() {
        super.onDestroy()

        mainNavigationPresenter.unsubscribeToScreenChanges()
    }

    override fun switchToScreen(screen: Screen, args: Bundle?) {
        getNavigationActionFromScreenId(screen)?.let { navAction ->
            navController?.navigate(navAction, args)
        }
    }


    private fun getNavigationActionFromScreenId(screen: Screen): Int? {
        return when (screen) {
            Screen.DETAIL -> R.id.action_SearchFragment_to_DetailFragment
            else -> null
        }
    }
}