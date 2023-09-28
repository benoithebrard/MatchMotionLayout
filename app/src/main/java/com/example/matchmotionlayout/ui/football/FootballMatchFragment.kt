package com.example.matchmotionlayout.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matchmotionlayout.R
import com.example.matchmotionlayout.databinding.FragmentFootballBinding
import com.example.matchmotionlayout.ui.utils.NestedMotionLayoutListener
import com.example.matchmotionlayout.ui.vertical.VerticalContentFragment

class FootballMatchFragment : Fragment() {

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

        viewBinding?.apply {
            NestedMotionLayoutListener(this).setup()
            rootContainer.setTransition(R.id.header_transition_legacy)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}