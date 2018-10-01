package eu.vmpay.owlquiz.activities.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.databinding.FragmentTimerBinding
import eu.vmpay.owlquiz.utils.InjectorUtils

class TimerFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTimerBinding>(layoutInflater, R.layout.fragment_timer, container, false)
//        val binding = FragmentTimerBinding.inflate(inflater, container, false)
        subscribeUi(binding)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentTimerBinding) {
        val factory = InjectorUtils.provideTimerViewModelFactory(requireContext())
        val viewModel = ViewModelProviders.of(this, factory)
                .get(TimerViewModel::class.java)
        binding.viewModel = viewModel
    }
}