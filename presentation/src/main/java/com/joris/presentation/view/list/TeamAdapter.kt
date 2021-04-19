package com.joris.presentation.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joris.fdj.R
import com.joris.presentation.presenter.SearchPresenter
import com.joris.presentation.gateway.ImageGateway
import org.koin.java.KoinJavaComponent.inject


internal class TeamAdapter(
    private val listContent: List<SearchPresenter.ListContent>,
    private val recyclerEventListener: RecyclerEventListener
) : RecyclerView.Adapter<TeamViewHolder>() {

    private val imageGateway by inject(
        clazz = ImageGateway::class.java,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun getItemCount() = listContent.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        imageGateway.loadImage(holder.teamImage, R.drawable.team_badge_placeholder, listContent[position].teamBadgeImageUrl)

        holder.itemView.setOnClickListener {
            recyclerEventListener.onRecyclerItemClick(
                RecyclerEventType.OPEN,
                listContent[position].teamName
            )
        }

    }
}