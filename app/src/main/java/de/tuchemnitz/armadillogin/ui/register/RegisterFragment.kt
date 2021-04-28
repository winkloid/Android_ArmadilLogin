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
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val userViewModel: UserDataViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            registerFragment = this@RegisterFragment
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER1)
    }

    fun goToNextView() {
        // before going t next fragment, check all values provided by user
        checkValues()
        findNavController().navigate(R.id.action_navigation_register1_to_navigation_register2)
    }

    private fun checkValues() {
        val lastname = binding?.registerInputLastnameEditText?.text.toString()
        val firstname = binding?.registerInputFirstnameEditText?.text.toString()
        val email = binding?.registerInputEmailEditText?.text.toString()

        //values for validating which of the user-provided values are failing
        var fnOk = false
        var lnOk = false
        var emailOk = false

        //validate user inputs
        if (!firstname.isNullOrBlank()) {
            fnOk = true
        }
        if (!lastname.isNullOrBlank()) {
            lnOk = true
        }
        emailOk = checkEmailFormat(email)
        Log.d("EMAIL", "$emailOk")

        // if everything is correct pass data to userViewModel, else: show errors
        if (fnOk && lnOk && emailOk) {
            binding?.registerInputFirstnameEditText?.error = null
            binding?.registerInputLastnameEditText?.error = null
            binding?.registerInputEmailEditText?.error = null
            userViewModel.setData(firstname, lastname, email)
        }
        else {
            showErrors(fnOk, lnOk, emailOk)
        }
    }

    private fun checkEmailFormat(email: String): Boolean {
        // first, check whether there is an null or blank string
        if (!email.isNullOrBlank()) {
            //if this is not the case, test whether the given string has a correct email format
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            //if string is blank or null
            return false
        }
    }

    private fun showErrors(fnOk: Boolean, lnOk: Boolean, emailOk: Boolean) {
        if (fnOk) {
            binding?.registerInputFirstnameEditText?.error = null
        } else {
            binding?.registerInputFirstnameEditText?.error = getString(R.string.simple_string_error)
        }

        if (lnOk) {
            binding?.registerInputLastnameEditText?.error = null
        } else {
            binding?.registerInputLastnameEditText?.error = getString(R.string.simple_string_error)
        }

        if (emailOk) {
            binding?.registerInputEmailEditText?.error = null
        } else {
            binding?.registerInputEmailEditText?.error = getString(R.string.email_string_error)
        }
    }
}