package com.joris.presentation.view.list

internal enum class RecyclerEventType {
    OPEN
}

internal interface RecyclerEventListener {
    fun onRecyclerItemClick(eventType: RecyclerEventType, teamName: String?)
}