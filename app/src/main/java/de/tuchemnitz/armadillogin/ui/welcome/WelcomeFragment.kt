package de.tuchemnitz.armadillogin.ui.welcome

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentHelpBinding
import de.tuchemnitz.armadillogin.databinding.FragmentWelcomeBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.ui.help.HelpViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {

    private var binding: FragmentWelcomeBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentBinding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@WelcomeFragment
            welcomeFragment = this@WelcomeFragment
            armadilloViewModel = sharedViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("D","Resuming")
        sharedViewModel.setFragmentStatus(FragmentStatus.WELCOME)
    }
    fun goToNextView() {
        findNavController().navigate(R.id.action_navigation_welcome_to_navigation_register_login)
    }
}