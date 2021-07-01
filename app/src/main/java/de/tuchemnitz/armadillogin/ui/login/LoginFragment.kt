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
 * A [Fragment] subclass used as login fragment.
 *
 * In this fragment the user is asked for the user name that he/she registered before.
 * After the input is confirmed, the application checks whether the input is valid and then sends the username to the server to have a session ID generated.
 */
class LoginFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_login.xml
     */
    private var binding: FragmentLoginBinding? = null

    /**
     * Shared [ArmadilloViewModel] for fragment status and settings.
     *
     * Manages the [FragmentStatus] value to display adapted resources in the Help tab. It also manages some variables to store user settings like color mode or font settings.
     */
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    /**
     * Shared [UserDataViewModel] for storing personal data during registration process.
     *
     * This data is not stored after the app is closed, nor does it serve the purpose of data collection at all.
     * The data is only used to make the registration process as genuine as possible by requesting data from the user that would also be requested during a real registration process.
     * This data includes, among other things, name, e-mail address and username.
     * In addition, the ViewModel interfaces with the FIDO2 API.
     */
    private val userViewModel: UserDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_login.xml.
     * You can find these variables declared in the <data> section of fragment_login.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@LoginFragment
            loginFragment = this@LoginFragment
            userModel = userViewModel
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [LoginFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [LoginFragment].
     * Announcing [LoginFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.LOGIN)
        view?.announceForAccessibility(getString(R.string.login_accessibility_label))
    }

    /**
     * Check whether the input value is valid and after this send the username to the FIDO2 server.
     *
     * Calls [checkValues] first to check whether the user entered a valid username
     * Calls [sendUserName] after this to send the provided username to the server and to navigate to next [Fragment] using [R.id.action_navigation_login1_to_navigation_login_key] action.
     */
    fun goToNextView() {
        val valuesCorrect = checkValues()
        if (valuesCorrect) {
            sendUserName()
        }
    }

    /**
     * Check whether the username entered by the user is valid.
     *
     * Check whether the username provided by the user is a string that is not null and not blank.
     * @return Boolean value to show whether the provided value is acceptable or not
     */
    private fun checkValues(): Boolean {
        val username = binding?.loginInputUsernameEditText?.text.toString()
        Log.d("UNAME", "$username")

        if (!username.isNullOrBlank()) {
            binding?.loginInputUsernameEditText?.error = null
            userViewModel.setUsername(username)
            return true
        } else {
            binding?.loginInputUsernameEditText?.error = getString(R.string.simple_string_error)
            return false
        }
    }

    /**
     * Send username to FIDO2 server to have a session ID generated.
     *
     * Uses sendUsername method from [userViewModel] to send the username provided by the user to the fido2/webauthn server.
     * After this is done, the user is redirected to [LoginKeyFragment].
     * Calls [userViewModel] setUsernameBeforePassword method to reset the bool that ensures that the username() method in AuthRepository is always executed before password() method.
     */
    private fun sendUserName() {
        Toast.makeText(
            activity,
            getString(R.string.register_summary_sending_username),
            Toast.LENGTH_SHORT
        ).show()

        // wait for username and password to be sent and then go to the next fragment
        userViewModel.sendUsername()
        userViewModel.usernameBeforePassword.observe(viewLifecycleOwner) { usernameBeforeNextTask ->
            if (usernameBeforeNextTask) {
                findNavController().navigate(R.id.action_navigation_login1_to_navigation_login_key)
                userViewModel.setUsernameBeforePassword()
            }
        }
    }
}