package com.example.matchmotionlayout.ui.viewmodel

import androidx.recyclerview.widget.RecyclerView
import com.example.matchmotionlayout.ui.adapter.MockHorizontalRecyclerAdapter
import com.example.matchmotionlayout.ui.adapter.MockVerticalRecyclerAdapter

data class MatchViewModel(
    val title: String = "football match",
    val verticalAdapter: RecyclerView.Adapter<MockVerticalRecyclerAdapter.ViewHolder> = MockVerticalRecyclerAdapter(),
    val horizontalAdapter: RecyclerView.Adapter<MockHorizontalRecyclerAdapter.ViewHolder> = MockHorizontalRecyclerAdapter()
)