package com.joris.presentation.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joris.fdj.R
import com.joris.presentation.presenter.SearchPresenter
import com.joris.presentation.view.helper.ImageDownloader

class TeamAdapter(
    private val teams: List<SearchPresenter.ListContent>,
    private val recyclerEventListener: RecyclerEventListener
) :
    RecyclerView.Adapter<TeamViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        // TODO : Add progress while team banner is loading
        ImageDownloader.loadBitmapIntoImageView(holder.teamImage, teams[position].teamBadgeImageUrl)

        holder.itemView.setOnClickListener {
            recyclerEventListener.onRecyclerItemClick(
                RecyclerEventType.OPEN,
                teams[position].teamName
            )
        }

    }
}