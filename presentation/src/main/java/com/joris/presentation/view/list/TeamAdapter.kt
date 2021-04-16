package com.joris.presentation.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joris.business.entity.Team
import com.joris.fdj.R
import com.joris.presentation.gateway.ImageGateway
import com.joris.presentation.gateway.ImageGatewayImpl
import org.koin.java.KoinJavaComponent

internal class TeamAdapter(private val teams: List<Team>, private val recyclerEventListener: RecyclerEventListener) :
    RecyclerView.Adapter<TeamViewHolder>() {

    private val imageGateway by KoinJavaComponent.inject(
        clazz = ImageGateway::class.java,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        // TODO : Add progress while team banner is loading
        imageGateway.loadImage(holder.teamImage, teams[position].badgeImageUrl)

        holder.itemView.setOnClickListener {
            recyclerEventListener.onRecyclerItemClick(RecyclerEventType.OPEN, teams[position].name)
        }

    }
}