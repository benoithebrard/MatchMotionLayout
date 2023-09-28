package com.example.matchmotionlayout.ui.utils

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import com.example.matchmotionlayout.databinding.FragmentFootballBinding

class NestedMotionLayoutListener(
    private val viewBinding: FragmentFootballBinding
) : MotionLayout.TransitionListener {

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        updateNestedMotionLayout(motionLayout)
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        updateNestedMotionLayout(motionLayout)
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        updateNestedMotionLayout(motionLayout)
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
        updateNestedMotionLayout(motionLayout)
    }

    fun setup() {
        viewBinding.apply {
            rootContainer.setTransitionListener(this@NestedMotionLayoutListener)
            (overallHeaderContainer.children.first() as MotionLayout).setTransitionListener(this@NestedMotionLayoutListener)
        }
    }

    private fun updateNestedMotionLayout(motionLayout: MotionLayout?) {
        motionLayout?.let { layout ->
            viewBinding.apply {
                if (layout.id == rootContainer.id) {
                    (overallHeaderContainer.children.first() as MotionLayout).progress =
                        layout.progress
                }
            }
        }
    }
}