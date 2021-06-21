package de.tuchemnitz.armadillogin.ui.finished

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentFinishedBinding
import de.tuchemnitz.armadillogin.databinding.FragmentWelcomeBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus


/**
 * A simple [Fragment] subclass.
 * Use the [FinishedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinishedFragment : Fragment() {
    private var binding: FragmentFinishedBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentFinishedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@FinishedFragment
            finishedFragment = this@FinishedFragment
            armadilloViewModel = sharedViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("D", "Resuming")
        sharedViewModel.setFragmentStatus(FragmentStatus.FINISHED)
    }

    fun backToWelcome() {
        findNavController().navigate(R.id.action_navigation_finished_to_navigation_welcome)
    }
}