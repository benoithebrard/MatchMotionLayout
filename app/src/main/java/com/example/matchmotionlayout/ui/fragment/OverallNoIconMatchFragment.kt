package com.example.matchmotionlayout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.matchmotionlayout.R
import com.example.matchmotionlayout.databinding.FragmentMatchBinding
import com.example.matchmotionlayout.ui.utils.NestedMotionLayoutListener
import com.example.matchmotionlayout.ui.utils.dpToPx
import com.example.matchmotionlayout.ui.vertical.VerticalContentFragment
import com.example.matchmotionlayout.ui.viewmodel.MatchViewModel

class OverallNoIconMatchFragment : Fragment() {

    private var viewBinding: FragmentMatchBinding? = null

    private var motionListener: NestedMotionLayoutListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMatchBinding.inflate(inflater).apply {
        viewModel = MatchViewModel()
    }.also { binding ->
        viewBinding = binding
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(
            R.id.scrollable_content,
            VerticalContentFragment()
        ).commit()

        viewBinding?.apply {
            val overallScoreboardMotionLayout =
                overallScoreboardContainer.children.first() as MotionLayout
            motionListener = NestedMotionLayoutListener(
                lifecycleOwner = viewLifecycleOwner,
                rootMotionLayout = rootContainer,
                firstNestedMotionLayout = overallScoreboardMotionLayout
            ).also { listener ->
                listener.setup()
            }
            rootContainer.setTransition(R.id.match_transition_overall)
            overallScoreboardMotionLayout.setTransition(R.id.scoreboard_transition_overall_no_icon)

            rootContainer.findViewById<ImageView>(R.id.first_team_icon).isVisible = false
            rootContainer.findViewById<ImageView>(R.id.second_team_icon).isVisible = false
            rootContainer.findViewById<ImageView>(R.id.first_team_form).isVisible = false
            rootContainer.findViewById<ImageView>(R.id.second_team_form).isVisible = false
            rootContainer.findViewById<ImageView>(R.id.extra_time).isVisible = true
            guidelineHeaderBottom.setGuidelineBegin(126.dpToPx.toInt())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        motionListener?.clear()
        viewBinding = null
    }
}