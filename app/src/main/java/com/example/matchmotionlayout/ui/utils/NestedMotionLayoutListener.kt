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

    private var nestedMotionLayout: MotionLayout? = null

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
        nestedMotionLayout = rootLayout.children.toList().findNestedMotionLayout()
        nestedMotionLayout?.setTransitionListener(this@NestedMotionLayoutListener)
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
        nestedMotionLayout?.removeTransitionListener(this@NestedMotionLayoutListener)
    }

    private fun updateNestedMotionLayouts(
        motionLayout: MotionLayout?,
        progress: Float? = null
    ) {
        if (motionLayout == null) return
        lastProgress = progress ?: motionLayout.progress
        lastProgress?.let {
            if (motionLayout.id == rootLayout.id) {
                rootLayout.children.filterIsInstance<MotionLayout>().firstOrNull()?.progress =
                    it
                nestedMotionLayout?.progress = it
                // forward progress to other nested views here
            }
        }
    }

    private tailrec fun List<View>.findNestedMotionLayout(): MotionLayout? {
        forEach {
            if (it is MotionLayout) return it
            if (it is ViewGroup) return it.children.toList().findNestedMotionLayout()
        }
        return null
    }
}