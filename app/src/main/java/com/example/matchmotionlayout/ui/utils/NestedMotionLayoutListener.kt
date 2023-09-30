package com.example.matchmotionlayout.ui.utils

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NestedMotionLayoutListener(
    private val lifecycleOwner: LifecycleOwner,
    private val rootMotionLayout: MotionLayout,
    private val firstNestedMotionLayout: MotionLayout? = null
) : MotionLayout.TransitionListener {

    private var lastProgress: Float? = null

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
        rootMotionLayout.setTransitionListener(this@NestedMotionLayoutListener)
        firstNestedMotionLayout?.setTransitionListener(this@NestedMotionLayoutListener)
        autoSnapWorkaround()
    }

    private fun autoSnapWorkaround() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(500L)
                    lastProgress?.let { progress ->
                        when {
                            rootMotionLayout.progress == 0f && progress > 0 -> {
                                rootMotionLayout.progress = progress
                                rootMotionLayout.transitionToStart()
                            }

                            rootMotionLayout.progress == 1f && progress < 1 -> {
                                rootMotionLayout.progress = progress
                                rootMotionLayout.transitionToEnd()
                            }
                        }
                    }
                }
            }
        }
    }

    fun clear() {
        rootMotionLayout.removeTransitionListener(this@NestedMotionLayoutListener)
        firstNestedMotionLayout?.removeTransitionListener(this@NestedMotionLayoutListener)
    }

    private fun updateNestedMotionLayouts(
        motionLayout: MotionLayout?,
        progress: Float? = null
    ) {
        if (motionLayout == null) return
        lastProgress = progress ?: motionLayout.progress
        lastProgress?.let {
            if (motionLayout.id == rootMotionLayout.id) {
                firstNestedMotionLayout?.progress = it
                // forward progress to other nested views here
            }
        }
    }
}