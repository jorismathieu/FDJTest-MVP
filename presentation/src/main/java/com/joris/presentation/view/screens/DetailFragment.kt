package com.joris.presentation.view.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.joris.fdj.R
import com.joris.presentation.gateway.ImageGateway
import com.joris.presentation.presenter.DetailsPresenter
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

internal class DetailFragment : Fragment(), DetailsPresenter.View {

    private val detailsPresenter: DetailsPresenter by inject(
        clazz = DetailsPresenter::class.java,
        parameters = { parametersOf(this@DetailFragment) })

    private val imageGateway by inject(
        clazz = ImageGateway::class.java,
    )

    // TODO : DataBinding to avoid boilerplate
    private lateinit var errorMessage: View
    private lateinit var progressBar: View
    private lateinit var teamImage: ImageView
    private lateinit var teamCountry: TextView
    private lateinit var teamLeague: TextView
    private lateinit var teamDescription: TextView

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


        savedInstanceState?.let {
            detailsPresenter.onRestoreState(savedInstanceState)
        } ?: run {
            detailsPresenter.getTeamDetails(arguments?.getString("teamName", null))
        }
    }

    override fun onViewStateChanged(viewState: DetailsPresenter.ViewState?) {
        viewState?.let {
            if (viewState.showError) errorMessage.visibility =
                View.VISIBLE else errorMessage.visibility = View.GONE
            if (viewState.showLoading) progressBar.visibility =
                View.VISIBLE else progressBar.visibility = View.GONE
            if (viewState.showContent) {
                teamImage.visibility = View.VISIBLE
                teamCountry.visibility = View.VISIBLE
                teamLeague.visibility = View.VISIBLE
                teamDescription.visibility = View.VISIBLE
                imageGateway.loadImage(
                    teamImage,
                    R.drawable.team_banner_placeholder,
                    viewState.content?.bannerImageUrl
                )
                teamCountry.text = viewState.content?.country
                teamLeague.text = viewState.content?.league
                teamDescription.text = viewState.content?.description
            } else {
                teamImage.visibility = View.GONE
                teamCountry.visibility = View.GONE
                teamLeague.visibility = View.GONE
                teamDescription.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        detailsPresenter.onSaveState(outState)
    }
}