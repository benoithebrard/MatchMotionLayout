package com.example.matchmotionlayout.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.matchmotionlayout.R

class DummyRecyclerAdapter : RecyclerView.Adapter<DummyRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.dummy_list_item, parent, false) as ConstraintLayout
        )
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = Unit

    class ViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

}