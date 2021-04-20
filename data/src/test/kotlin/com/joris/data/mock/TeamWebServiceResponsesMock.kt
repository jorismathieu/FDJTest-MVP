package com.joris.data.mock

import okhttp3.mockwebserver.MockResponse

// TODO : Add all type of response we could get, list must be exhaustive
object TeamWebServiceResponsesMock {

    val SEARCH_TEAMS_MOCK by lazy {
        MockResponse().setBody(
            "{\n" +
                    "\"teams\": [\n" +
                    "{\n" +
                    "\"strTeam\": \"team1\",\n" +
                    "\"strLeague\": \"league1\",\n" +
                    "\"strDescriptionFR\": \"description1\",\n" +
                    "\"strCountry\": \"country1\",\n" +
                    "\"strTeamBadge\": \"badge1\",\n" +
                    "\"strTeamBanner\": \"banner1\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n"
        )
    }

    val SEARCH_TEAMS_EMPTY by lazy {
        MockResponse().setBody(
            "{\n" +
                    "\"teams\": []\n" +
                    "}\n"
        )
    }

    val SEARCH_TEAMS_PARSING_ERROR by lazy {
        MockResponse().setBody(
            "{}"
        )
    }

    val SEARCH_TEAMS_ERROR by lazy {
        MockResponse().setResponseCode(500)
    }

    val SEARCH_ALL_TEAMS_MOCK by lazy {
        MockResponse().setBody(
            "{\n" +
                    "\"teams\": [\n" +
                    "{\n" +
                    "\"strTeam\": \"team1\",\n" +
                    "\"strLeague\": \"league1\",\n" +
                    "\"strDescriptionFR\": \"description1\",\n" +
                    "\"strCountry\": \"country1\",\n" +
                    "\"strTeamBadge\": \"badge1\",\n" +
                    "\"strTeamBanner\": \"banner1\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"strTeam\": \"team2\",\n" +
                    "\"strLeague\": \"league2\",\n" +
                    "\"strDescriptionFR\": \"description2\",\n" +
                    "\"strCountry\": \"country2\",\n" +
                    "\"strTeamBadge\": \"badge2\",\n" +
                    "\"strTeamBanner\": \"banner2\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n"
        )
    }

    val SEARCH_ALL_TEAMS_EMPTY by lazy {
        MockResponse().setBody(
            "{\n" +
                    "\"teams\": []\n" +
                    "}\n"
        )
    }

    val SEARCH_ALL_TEAMS_PARSING_ERROR by lazy {
        MockResponse().setBody(
            "{}"
        )
    }

    val SEARCH_ALL_TEAMS_ERROR by lazy {
        MockResponse().setResponseCode(500)
    }
}