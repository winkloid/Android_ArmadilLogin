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
            lifecycleOwner = this@RegisterFragment
            registerFragment = this@RegisterFragment
            armadilloViewModel = sharedViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER1)

        // check whether there already are user data different than default data in userViewModel
        // if so, the corresponding EditText fields are filled in
        if (!userViewModel.firstname.value.equals("")) {
            binding?.registerInputFirstnameEditText?.setText(userViewModel.firstname.value.toString())
        }
        if (!userViewModel.lastname.value.equals("")) {
            binding?.registerInputLastnameEditText?.setText(userViewModel.lastname.value.toString())
        }
        if (!userViewModel.email.value.equals("")) {
            binding?.registerInputEmailEditText?.setText(userViewModel.email.value.toString())
        }
    }

    fun goToNextView() {
        // before going t next fragment, check all values provided by user
        val valuesCorrect = checkValues()
        if (valuesCorrect) {
            findNavController().navigate(R.id.action_navigation_register1_to_navigation_register2)
        }
    }

    private fun checkValues(): Boolean {
        val lastname = binding?.registerInputLastnameEditText?.text.toString()
        val firstname = binding?.registerInputFirstnameEditText?.text.toString()
        val email = binding?.registerInputEmailEditText?.text.toString()

        //values for validating which of the user-provided values are failing
        var fnOk = false
        var lnOk = false
        var emailOk = checkEmailFormat(email)

        //validate user inputs
        if (!firstname.isNullOrBlank()) {
            fnOk = true
        }
        if (!lastname.isNullOrBlank()) {
            lnOk = true
        }

        Log.d("EMAIL", "$emailOk")

        // if everything is correct pass data to userViewModel, else: show errors
        if (fnOk && lnOk && emailOk) {
            binding?.registerInputFirstnameEditText?.error = null
            binding?.registerInputLastnameEditText?.error = null
            binding?.registerInputEmailEditText?.error = null
            userViewModel.setData(firstname, lastname, email)
            return true
        } else {
            showErrors(fnOk, lnOk, emailOk)
            return false
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
        // check for every field whether the user-provided value is acceptable or not and if not show errors
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