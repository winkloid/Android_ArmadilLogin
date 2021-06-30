package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterUserNameBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A [Fragment] subclass used as register username fragment.
 *
 * In this fragment the user is able to choose a user name for his/her account.
 * The string entered by the user is checked for certain criteria:
 * The input field must be filled before the user can continue. Also, the username must contain only characters from 'a' to 'z', 'A' to 'Z' and '0' to '9'.
 */
class RegisterUserNameFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_register_username.xml
     */
    private var binding: FragmentRegisterUserNameBinding? = null

    /**
     * Shared [UserDataViewModel] for storing personal data during registration process.
     *
     * This data is not stored after the app is closed, nor does it serve the purpose of data collection at all.
     * The data is only used to make the registration process as genuine as possible by requesting data from the user that would also be requested during a real registration process.
     * This data includes, among other things, name, e-mail address and username.
     * In addition, the ViewModel interfaces with the FIDO2 API.
     */
    private val userViewModel: UserDataViewModel by activityViewModels()

    /**
     * Shared [ArmadilloViewModel] for fragment status and settings.
     *
     * Manages the [FragmentStatus] value to display adapted resources in the Help tab. It also manages some variables to store user settings like color mode or font settings.
     */
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterUserNameBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel], announce [RegisterUserNameFragment] to screen readers when it is resumed and prefill edit text field if a username is already stored in [sharedViewModel].
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [RegisterUserNameFragment].
     * Announcing [RegisterUserNameFragment] when it is resumed increases the accessibility of the application.
     * Prefilling is useful if the user only wants to correct some values.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER2)
        if (!userViewModel.username.value.equals("")) {
            binding?.registerInputUsernameEditText?.setText(userViewModel.username.value.toString())
        }
        view?.announceForAccessibility(getString(R.string.register_username_accessibility_label))
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_register_username.xml.
     * You can find these variables declared in the <data> section of fragment_register_username.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterUserNameFragment
            registerUserNameFragment = this@RegisterUserNameFragment
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Check whether given input is valid - if so, go to next [Fragment].
     *
     * Calls [checkValues] to check whether the given input string only contains valid characters.
     * Calls [findNavController] to navigate to next fragment.
     */
    fun goToNextView() {
        val valuesCorrect = checkValues()
        if (valuesCorrect) {
            findNavController().navigate(R.id.action_navigation_register2_to_navigation_register_summary)
        }
    }

    /**
     * Checks whether given input string contains valid characters only.
     *
     * Calls [isNullOrBlank] to check whether there are any letters or numbers in the given input [String].
     * Calls [isAlphaNumeric] to check whether only characters are in input string that are allowed: 'a' to 'z', 'A' to 'Z' and '0' to '9'.
     * If the input provided by the user is blank or if it contains characters that are not allowed, an error will be shown in the corresponding edit text field to inform the user.
     */
    private fun checkValues(): Boolean {
        val username = binding?.registerInputUsernameEditText?.text.toString()

        // check whether username input field is blank or null and whether it is alphanumeric
        val usernameNullOrBlank = username.isNullOrBlank()
        if (!usernameNullOrBlank && isAlphaNumeric(username)) {
            binding?.registerInputUsernameEditText?.error = null
            userViewModel.setUsername(username)
            return true
        } else {
            if (usernameNullOrBlank) {
                binding?.registerInputUsernameEditText?.error =
                    getString(R.string.simple_string_error)
            } else {
                binding?.registerInputUsernameEditText?.error =
                    getString(R.string.not_alphanumeric_string_error)
            }
            return false
        }
    }

    /**
     * Checks whether [line] is a [String] which contains alphanumeric values only.
     *
     * Checks for each character of the string whether it is in the set of allowed characters.
     *
     * @param line The input string provided b the user.
     * @return true if [line] is alphanumeric and false if [line] is not alphanumeric.
     */
    private fun isAlphaNumeric(line: String): Boolean {
        if (line.isNullOrBlank()) {
            return false
        } else {
            for (char in line) {
                if (char !in 'a'..'z' && char !in 'A'..'Z' && char !in '0'..'9') {
                    return false
                }
            }
            return true
        }
    }
}