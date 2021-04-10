package com.joris.data.api

import com.google.gson.annotations.SerializedName
import com.joris.data.entity.TeamImpl


class TeamListResponse {
    @SerializedName("teams")
    var teams: List<TeamImpl>? = null
}