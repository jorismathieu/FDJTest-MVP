package com.joris.presentation.view.list

enum class RecyclerEventType {
    OPEN
}

interface RecyclerEventListener {
    fun onRecyclerItemClick(eventType: RecyclerEventType, teamName: String?)
}