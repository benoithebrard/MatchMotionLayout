package com.example.matchmotionlayout.ui.utils

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NestedMotionLayoutListener(
    private val lifecycleOwner: LifecycleOwner,
    private val rootLayout: MotionLayout
) : MotionLayout.TransitionListener {

    private var lastProgress: Float? = null

    private val nestedMotionLayouts: List<MotionLayout> =
        rootLayout.children.toList().findNestedMotionLayouts()

    override fun onTransitionStarted(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int
    ) {
        updateNestedMotionLayouts(motionLayout)
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        updateNestedMotionLayouts(motionLayout, progress)
    }

    override fun onTransitionCompleted(
        motionLayout: MotionLayout?,
        currentId: Int
    ) {
        updateNestedMotionLayouts(motionLayout)
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
        updateNestedMotionLayouts(motionLayout, progress)
    }

    fun setup() {
        rootLayout.setTransitionListener(this@NestedMotionLayoutListener)
        nestedMotionLayouts.forEach { it.setTransitionListener(this@NestedMotionLayoutListener) }
        autoSnapWorkaround()
    }

    private fun autoSnapWorkaround() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(500L)
                    lastProgress?.let { progress ->
                        when {
                            rootLayout.progress == 0f && progress > 0 -> {
                                rootLayout.progress = progress
                                rootLayout.transitionToStart()
                            }

                            rootLayout.progress == 1f && progress < 1 -> {
                                rootLayout.progress = progress
                                rootLayout.transitionToEnd()
                            }
                        }
                    }
                }
            }
        }
    }

    fun clear() {
        rootLayout.removeTransitionListener(this@NestedMotionLayoutListener)
        nestedMotionLayouts.forEach { layout ->
            layout.removeTransitionListener(this@NestedMotionLayoutListener)
        }
    }

    private fun updateNestedMotionLayouts(
        motionLayout: MotionLayout?,
        progress: Float? = null
    ) {
        if (motionLayout == null) return

        lastProgress = (progress ?: motionLayout.progress).also { motionProgress ->
            if (motionLayout.id == rootLayout.id) {
                nestedMotionLayouts.forEach { layout ->
                    layout.progress = motionProgress
                }
            }
        }
    }

    private fun List<View>.findNestedMotionLayouts(): List<MotionLayout> =
        fold(emptyList()) { motionLayouts, view ->
            when (view) {
                is MotionLayout -> {
                    motionLayouts + view
                }

                is ViewGroup -> {
                    motionLayouts + view.children.toList().findNestedMotionLayouts()
                }

                else -> motionLayouts
            }
        }
}