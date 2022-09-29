package com.example.matchmotionlayout.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matchmotionlayout.databinding.FragmentFootballBinding

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

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}