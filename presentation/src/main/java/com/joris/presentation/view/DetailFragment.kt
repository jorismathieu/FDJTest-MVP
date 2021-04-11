package com.joris.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.joris.business.entity.Team
import com.joris.fdj.R
import com.joris.presentation.presenter.DetailsPresenter
import com.joris.presentation.presenter.SearchPresenter
import com.joris.presentation.view.helper.ImageDownloader
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class DetailFragment : Fragment(), DetailsPresenter.View {

    private val searchPresenter: DetailsPresenter by inject(
        clazz = DetailsPresenter::class.java,
        parameters = { parametersOf(this@DetailFragment) })

    // TODO : DataBinding to avoid boilerplate
    private lateinit var errorMessage : View
    private lateinit var progressBar : View
    private lateinit var teamImage : ImageView
    private lateinit var teamCountry : TextView
    private lateinit var teamLeague : TextView
    private lateinit var teamDescription : TextView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorMessage = view.findViewById(R.id.error_text)
        progressBar = view.findViewById(R.id.progress_bar)
        teamImage = view.findViewById(R.id.team_image)
        teamCountry = view.findViewById(R.id.team_country)
        teamLeague = view.findViewById(R.id.team_league)
        teamDescription = view.findViewById(R.id.team_description)

        searchPresenter.getTeamDetails(arguments?.getString("teamName", null))
    }

    override fun onError() {
        showError()
    }

    override fun onSuccess(team: Team) {
        showContent()

        // TODO : Add progress while team banner is loading
        ImageDownloader.loadBitmapIntoImageView(teamImage, team.bannerImageUrl)
        teamCountry.text = team.country
        teamLeague.text = team.league
        teamDescription.text = team.description
    }

    private fun showContent() {
        errorMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
        teamImage.visibility = View.VISIBLE
        teamCountry.visibility = View.VISIBLE
        teamLeague.visibility = View.VISIBLE
        teamDescription.visibility = View.VISIBLE
    }

    private fun showError() {
        errorMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        teamImage.visibility = View.GONE
        teamCountry.visibility = View.GONE
        teamLeague.visibility = View.GONE
        teamDescription.visibility = View.GONE
    }
}