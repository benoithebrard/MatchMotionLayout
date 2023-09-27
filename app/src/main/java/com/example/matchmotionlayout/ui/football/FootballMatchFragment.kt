package com.example.matchmotionlayout.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.matchmotionlayout.R
import com.example.matchmotionlayout.databinding.FragmentFootballBinding
import com.example.matchmotionlayout.ui.vertical.VerticalContentFragment

class FootballMatchFragment : Fragment(), MotionLayout.TransitionListener {

    private var viewBinding: FragmentFootballBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFootballBinding.inflate(inflater).apply {
        viewModel = FootballMatchViewModel()
    }.also { binding ->
        viewBinding = binding
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(
            R.id.scrollable_content,
            VerticalContentFragment()
        ).commit()

        setUpMotionLayoutListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun updateNestedMotionLayout(motionLayout: MotionLayout?) {
        motionLayout?.let { layout ->
            viewBinding?.apply {
                if (layout.id == rootContainer.id) {
                    (overallHeaderContainer.children.first() as MotionLayout).progress =
                        layout.progress
                }
            }
        }
    }

    private fun setUpMotionLayoutListener() {
        viewBinding?.apply {
            (rootContainer as MotionLayout).setTransitionListener(this@FootballMatchFragment)
            (overallHeaderContainer.children.first() as MotionLayout).setTransitionListener(this@FootballMatchFragment)
        }
    }

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
}