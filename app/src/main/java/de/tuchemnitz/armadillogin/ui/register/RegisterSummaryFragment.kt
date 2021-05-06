package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterBinding
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterSummaryBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterSummaryFragment : Fragment() {
    private var binding: FragmentRegisterSummaryBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val userViewModel: UserDataViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterSummaryFragment
            registerSummaryFragment = this@RegisterSummaryFragment
            userDataModel = userViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_SUMMARY)
    }

    fun goBackToRegister() {
        findNavController().navigate(R.id.action_navigation_register_summary_to_navigation_register1)
    }

    fun goToNextFragment() {
        Toast.makeText(activity, getString(R.string.register_summary_sending_username), Toast.LENGTH_SHORT).show()

        // wait for username and password to be sent and then go to the next fragment
        userViewModel.sendUsername()
        userViewModel.sendingUsername.observe(viewLifecycleOwner) { sendingUsername ->
            if(sendingUsername) {}
            else {
                userViewModel.sendPassword()
                userViewModel.sendingPassword.observe(viewLifecycleOwner) { sendingPassword ->
                    if(sendingPassword) {}
                    else {
                        findNavController().navigate(R.id.action_navigation_register_summary_to_navigation_register_key)
                    }
                }
            }
        }
    }
}