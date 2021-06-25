package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterBinding
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterUserNameBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterUserName.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterUserNameFragment : Fragment() {
    private var binding: FragmentRegisterUserNameBinding? = null
    private val userViewModel: UserDataViewModel by activityViewModels()
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterUserNameBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER2)
        if (!userViewModel.username.value.equals("")) {
            binding?.registerInputUsernameEditText?.setText(userViewModel.username.value.toString())
        }
        view?.announceForAccessibility(getString(R.string.register_username_accessibility_label))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterUserNameFragment
            registerUserNameFragment = this@RegisterUserNameFragment
            armadilloViewModel = sharedViewModel
        }
    }

    fun goToNextView() {
        val valuesCorrect = checkValues()
        if (valuesCorrect) {
            findNavController().navigate(R.id.action_navigation_register2_to_navigation_register_summary)
        }
    }

    private fun checkValues(): Boolean {
        val username = binding?.registerInputUsernameEditText?.text.toString()
        Log.d("UNAME", "$username")

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
     * Checks whether [line] is a String which contains alphanumeric values only
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