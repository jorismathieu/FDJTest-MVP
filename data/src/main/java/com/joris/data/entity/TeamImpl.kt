package com.joris.data.entity

import com.google.gson.annotations.SerializedName
import com.joris.business.entity.Team


data class TeamImpl(
    @SerializedName("strTeam")
    override val name: String?,

    @SerializedName("strTeamBadge")
    override val badgeImageUrl: String?,

    @SerializedName("strTeamBanner")
    override val bannerImageUrl: String?,

    @SerializedName("strCountry")
    override val country: String?,

    @SerializedName("strLeague")
    override val league: String?,

    @SerializedName("strDescriptionFR")
    override val description: String?
) : Team