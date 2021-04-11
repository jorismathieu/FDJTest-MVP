package com.joris.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joris.business.entity.Team
import com.joris.business.usecase.GetTeamDetailsUseCase
import com.joris.fdj.R
import com.joris.presentation.presenter.SearchPresenter
import com.joris.presentation.presenter.SearchPresenterImpl
import kotlinx.coroutines.*
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.CoroutineContext

class SearchFragment : Fragment(), SearchPresenter.View {

    private val searchPresenter: SearchPresenter by inject(clazz = SearchPresenter::class.java, parameters = { parametersOf(this@SearchFragment) })

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchPresenter.onLeagueNameSubmitted("French Ligue 1")
    }

    override fun onDestroyView() {
        super.onDestroyView()

        searchPresenter.cleanup()
    }

    override fun onInternalErrorReceived() {

    }

    override fun onDataAccessErrorReceived() {

    }

    override fun onTeamListChanged(teams: List<Team>) {
        for (team in teams) {
            Log.e("Test", "team : $team")
        }
    }
}