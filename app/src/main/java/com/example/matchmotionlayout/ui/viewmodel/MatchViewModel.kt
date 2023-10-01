package com.example.matchmotionlayout.ui.viewmodel

import androidx.recyclerview.widget.RecyclerView
import com.example.matchmotionlayout.ui.adapter.MockRecyclerAdapter

data class MatchViewModel(
    val title: String = "football match",
    val adapter: RecyclerView.Adapter<MockRecyclerAdapter.ViewHolder> = MockRecyclerAdapter()
)