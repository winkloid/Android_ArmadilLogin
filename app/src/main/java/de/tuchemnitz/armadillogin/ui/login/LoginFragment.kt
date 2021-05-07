package de.tuchemnitz.armadillogin.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentLoginBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val userViewModel: UserDataViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@LoginFragment
            loginFragment = this@LoginFragment
            userModel = userViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.LOGIN)
    }

    /**
     * Is called via data binding when the Button is Pressed
     * Calls [checkValues] first to check whether the user entered a valid username
     * Calls [sendUserName] after this to send the provided username to the server and to navigate using [R.id.action_navigation_login1_to_navigation_login_key] action
     */
    fun goToNextView() {
        val valuesCorrect = checkValues()
        if(valuesCorrect) {
            sendUserName()
        }
    }

    /**
     * Check whether the username provided by the user is a string that is not null and not blank
     * returns Boolean value to show whether the provided value is acceptable or not
     */
    private fun checkValues(): Boolean {
        val username = binding?.loginInputUsernameEditText?.text.toString()
        Log.d("UNAME", "$username")

        if(!username.isNullOrBlank()) {
            binding?.loginInputUsernameEditText?.error = null
            userViewModel.setUsername(username)
            return true
        } else {
            binding?.loginInputUsernameEditText?.error = getString(R.string.simple_string_error)
            return false
        }
    }

    /**
     * Uses sendUsername method from [userViewModel] to send the username provided by the user to the fido2/webauthn server.
     * After this is done, the user is redirected to LoginKeyFragment
     * Calls [userViewModel] method to reset the bool that ensures that the username() method in AuthRepository is always executed before password() method is executed when user registers
     */
    private fun sendUserName() {
        Toast.makeText(activity, getString(R.string.register_summary_sending_username), Toast.LENGTH_SHORT).show()

        // wait for username and password to be sent and then go to the next fragment
        userViewModel.sendUsername()
        userViewModel.usernameBeforePassword.observe(viewLifecycleOwner) { usernameBeforeNextTask ->
            if(usernameBeforeNextTask) {
                findNavController().navigate(R.id.action_navigation_login1_to_navigation_login_key)
                userViewModel.setUsernameBeforePassword()
            }
        }
    }
}