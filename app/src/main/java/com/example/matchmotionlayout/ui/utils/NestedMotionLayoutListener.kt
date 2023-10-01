package com.example.matchmotionlayout.ui.utils

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Listen to progress from a root [MotionLayout], and propagate it to its children [MotionLayout]
 *
 * @param lifecycleOwner: the fragment lifecycle
 * @param rootLayout: the main root [MotionLayout]
 * @param subRootLayout: a sub [MotionLayout] that contains a [RecyclerView] of [MotionLayout]
 */
class NestedMotionLayoutListener(
    private val lifecycleOwner: LifecycleOwner,
    private val rootLayout: MotionLayout,
    private val subRootLayout: MotionLayout? = null
) : MotionLayout.TransitionListener {

    private var lastProgress: Float? = null

    private var nestedMotionLayouts: Set<MotionLayout> = emptySet()

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

    init {
        rootLayout.doOnLayout {
            rootLayout.setTransitionListener(this@NestedMotionLayoutListener)
            nestedMotionLayouts = rootLayout.children.toList().findNestedMotionLayouts().toSet()
            nestedMotionLayouts += subRootLayout?.children?.toList()?.findNestedMotionLayouts()
                ?.toSet()
                ?: emptySet()
            nestedMotionLayouts.forEach { motionLayout ->
                motionLayout.setTransitionListener(this@NestedMotionLayoutListener)
            }
            autoSnapWorkaround()
        }
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
                nestedMotionLayouts.forEach { motionLayout ->
                    motionLayout.progress = motionProgress
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

                is RecyclerView -> {
                    val childViews = (0 until view.childCount).map { view.getChildAt(it) }
                    motionLayouts + childViews.findNestedMotionLayouts()
                }

                is ViewGroup -> {
                    motionLayouts + view.children.toList().findNestedMotionLayouts()
                }

                else -> motionLayouts
            }
        }
}