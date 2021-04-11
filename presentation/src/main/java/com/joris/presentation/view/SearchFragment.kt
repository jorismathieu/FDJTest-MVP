package com.joris.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.joris.business.entity.Team
import com.joris.fdj.R
import com.joris.presentation.presenter.SearchPresenter
import kotlinx.coroutines.*
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class SearchFragment : Fragment(), SearchPresenter.View {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorMessage = view.findViewById(R.id.error_text)
        progressBar = view.findViewById(R.id.progress_bar)
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("Test", "onQueryTextSubmit : $query")
                searchPresenter.onLeagueNameSubmitted(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.e("Test", "onQueryTextChange : $newText")
                searchPresenter.onLeagueNameSubmitted(newText)
                return true
            }

        })
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
        for (team in teams) {
            Log.e("Test", "team : $team")
        }
        recyclerView.visibility = View.VISIBLE
    }

    override fun onHideContent() {
        recyclerView.visibility = View.GONE
    }
}