package com.example.matchmotionlayout.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.matchmotionlayout.R

class MockHorizontalRecyclerAdapter :
    RecyclerView.Adapter<MockHorizontalRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.mock_horizontal_list_item, parent, false) as ConstraintLayout
        )
    }

    override fun getItemCount(): Int {
        return 50
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = Unit

    class ViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

}