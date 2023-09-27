package com.example.matchmotionlayout.ui.football

import androidx.recyclerview.widget.RecyclerView
import com.example.matchmotionlayout.ui.adapter.DummyRecyclerAdapter

data class FootballMatchViewModel(
    val title: String = "football match",
    val adapter: RecyclerView.Adapter<DummyRecyclerAdapter.ViewHolder> = DummyRecyclerAdapter()
)