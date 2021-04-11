package com.joris.presentation.view.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joris.business.entity.Team
import com.joris.fdj.R
import com.joris.presentation.presenter.SearchPresenter
import com.joris.presentation.view.list.RecyclerEventListener
import com.joris.presentation.view.list.RecyclerEventType
import com.joris.presentation.view.list.TeamAdapter
import kotlinx.coroutines.*
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import java.lang.Runnable


class SearchFragment : Fragment(), SearchPresenter.View, RecyclerEventListener {

    private val searchPresenter: SearchPresenter by inject(
        clazz = SearchPresenter::class.java,
        parameters = { parametersOf(this@SearchFragment) })

    // TODO : DataBinding to avoid boilerplate
    private lateinit var errorMessage: View
    private lateinit var progressBar: View
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorMessage = view.findViewById(R.id.error_text)
        progressBar = view.findViewById(R.id.progress_bar)
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            private var runnable: Runnable? = null

            private fun postSubmit(query: String?) {
                runnable = Runnable { searchPresenter.onLeagueNameSubmitted(query) }
                searchView.postDelayed(runnable, 1000) // We delay by 1 sec
            }

            private fun cancelSubmit() {
                searchView.removeCallbacks(runnable)
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                cancelSubmit()
                searchPresenter.onLeagueNameSubmitted(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cancelSubmit()
                postSubmit(newText)
                return true
            }

        })
    }

    override fun onRecyclerItemClick(eventType: RecyclerEventType, teamName: String?) {
        searchPresenter.onTeamSelected(teamName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchPresenter.cleanup()
    }

    override fun onShowError() {
        errorMessage.visibility = View.VISIBLE
    }

    override fun onHideError() {
        errorMessage.visibility = View.GONE
    }

    override fun onShowProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onShowContent(teams: List<Team>) {
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = TeamAdapter(teams, this)
    }

    override fun onHideContent() {
        recyclerView.visibility = View.GONE
    }
}