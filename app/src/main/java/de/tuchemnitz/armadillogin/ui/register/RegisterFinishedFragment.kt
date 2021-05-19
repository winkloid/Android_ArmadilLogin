package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterFinishedBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFinishedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFinishedFragment : Fragment() {
    private var binding: FragmentRegisterFinishedBinding? = null
    private val userViewModel: UserDataViewModel by activityViewModels()
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterFinishedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_FINISHED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterFinishedFragment
            registerFinishedFragment = this@RegisterFinishedFragment
            armadilloViewModel = sharedViewModel
        }
    }

    fun backToStart() {
        userViewModel.signOutOnErrorOrFinished()
        findNavController().navigate(R.id.action_navigation_register_finished_to_navigation_register_login)
    }
}