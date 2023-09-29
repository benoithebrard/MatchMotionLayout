package com.example.matchmotionlayout.ui.viewmodel

import androidx.recyclerview.widget.RecyclerView
import com.example.matchmotionlayout.ui.adapter.DummyRecyclerAdapter

data class MatchViewModel(
    val title: String = "football match",
    val adapter: RecyclerView.Adapter<DummyRecyclerAdapter.ViewHolder> = DummyRecyclerAdapter()
)