package com.joris.presentation.view.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.joris.business.entity.Team
import com.joris.fdj.R
import com.joris.presentation.gateway.ImageGateway
import com.joris.presentation.presenter.DetailsPresenter
import com.joris.presentation.gateway.ImageGatewayImpl
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

internal class DetailFragment : Fragment(), DetailsPresenter.View {

    private val searchPresenter by inject(
        clazz = DetailsPresenter::class.java,
        parameters = { parametersOf(this@DetailFragment) })

    private val imageGateway by inject(
        clazz = ImageGateway::class.java,
    )

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

    override fun onShowContent(team: Team) {
        teamImage.visibility = View.VISIBLE
        teamCountry.visibility = View.VISIBLE
        teamLeague.visibility = View.VISIBLE
        teamDescription.visibility = View.VISIBLE

        // TODO : Add progress while team banner is loading
        imageGateway.loadImage(teamImage, team.bannerImageUrl)
        teamCountry.text = team.country
        teamLeague.text = team.league
        teamDescription.text = team.description
    }

    override fun onHideContent() {
        teamImage.visibility = View.GONE
        teamCountry.visibility = View.GONE
        teamLeague.visibility = View.GONE
        teamDescription.visibility = View.GONE
    }
}