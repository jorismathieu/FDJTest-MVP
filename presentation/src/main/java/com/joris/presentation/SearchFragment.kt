package com.joris.presentation

import android.os.Bundle
import android.provider.Contacts
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.fdj.R
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SearchFragment : Fragment(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val getTeamsFromLeagueUseCase: GetTeamsFromLeagueUseCase by inject(GetTeamsFromLeagueUseCase::class.java)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        launch {
//            Log.e("Test", "Request")
//            val teams = getTeamsFromLeagueUseCase.execute(GetTeamsFromLeagueUseCase.Input(league = "French Ligue 1")).data
//            Log.e("Test", "teams : $teams")
//            withContext(Dispatchers.Main) {
//                for (team in teams) {
//                    Log.e("Test", "team name = ${team.name}")
//                }
//            }
//        }
    }
}