package com.joris.business.entity

// Abstract definition of a team (at least the fields we use)
interface Team {
    val name: String?
    val badgeImageUrl: String?
    val bannerImageUrl: String?
    val country: String?
    val league: String?
    val description: String?
}