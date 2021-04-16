package com.joris.presentation.view.list

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.joris.fdj.R


internal class TeamViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var teamImage : ImageView = view.findViewById(R.id.team_image)
}