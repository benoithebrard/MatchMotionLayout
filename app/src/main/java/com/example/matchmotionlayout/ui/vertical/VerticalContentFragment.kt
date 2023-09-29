package com.example.matchmotionlayout.ui.vertical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.matchmotionlayout.databinding.FragmentVerticalContentBinding
import com.example.matchmotionlayout.ui.viewmodel.MatchViewModel

class VerticalContentFragment : Fragment() {

    private var viewBinding: FragmentVerticalContentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalContentBinding.inflate(inflater).apply {
        viewModel = MatchViewModel()
    }.also { binding ->
        viewBinding = binding
    }.root


    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}