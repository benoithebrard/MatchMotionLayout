package com.example.matchmotionlayout.ui.utils

import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.matchmotionlayout.databinding.FragmentMatchBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NestedMotionLayoutListener(
    private val lifecycleOwner: LifecycleOwner,
    private val viewBinding: FragmentMatchBinding
) : MotionLayout.TransitionListener {

    private var savedProgress: Float? = null

    override fun onTransitionStarted(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int
    ) {
        Log.d(
            "NestedMotionLayout",
            "--- onTransitionStarted"
        )
        updateNestedMotionLayout(motionLayout)
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        Log.d(
            "NestedMotionLayout",
            "--- onTransitionChange"
        )
        updateNestedMotionLayout(motionLayout, progress)
    }

    override fun onTransitionCompleted(
        motionLayout: MotionLayout?,
        currentId: Int
    ) {
        Log.d(
            "NestedMotionLayout",
            "--- onTransitionCompleted"
        )
        updateNestedMotionLayout(motionLayout)
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
        Log.d(
            "NestedMotionLayout",
            "--- onTransitionTrigger"
        )
        updateNestedMotionLayout(motionLayout, progress)
    }

    fun setup() {
        Log.d(
            "NestedMotionLayout",
            "--- setup ---"
        )
        viewBinding.apply {
            rootContainer.setTransitionListener(this@NestedMotionLayoutListener)
            (overallScoreboardContainer.children.first() as MotionLayout).setTransitionListener(this@NestedMotionLayoutListener)
        }
        applyTransitionWorkaround()
    }

    private fun applyTransitionWorkaround() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(500L)
                    viewBinding.apply {
                        Log.d(
                            "NestedMotionLayout",
                            "poll progress: ${rootContainer.progress}"
                        )
                        savedProgress?.let { progress ->
                            if (rootContainer.progress == 0f && progress > 0) {
                                Log.d(
                                    "NestedMotionLayout",
                                    ">> transition to start"
                                )
                                rootContainer.progress = progress
                                rootContainer.transitionToStart()
                            }
                        }
                    }
                }
            }
        }
    }

    fun clear() {
        Log.d(
            "NestedMotionLayout",
            "--- clear ---"
        )
        viewBinding.apply {
            rootContainer.removeTransitionListener(this@NestedMotionLayoutListener)
            (overallScoreboardContainer.children.first() as MotionLayout).removeTransitionListener(
                this@NestedMotionLayoutListener
            )
        }
    }

    private fun updateNestedMotionLayout(
        motionLayout: MotionLayout?,
        progress: Float? = null
    ) {
        Log.d(
            "NestedMotionLayout",
            "updated progress=$progress motion.progress=${motionLayout?.progress} motion.id=${motionLayout?.id}"
        )
        motionLayout?.let { layout ->
            savedProgress = progress ?: layout.progress
            savedProgress?.let { resolvedProgress ->
                viewBinding.apply {
                    if (layout.id == rootContainer.id) {
                        (overallScoreboardContainer.children.first() as MotionLayout).progress =
                            resolvedProgress
                    }
                }
            }
        }
    }
}