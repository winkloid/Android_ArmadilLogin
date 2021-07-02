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
 * A [Fragment] subclass used as register fragment.
 *
 * In this fragment the user is asked to provide some personal data. This data is neither stored permanently nor sent to a server.
 * The provision of this data serves to make the registration process as genuine as possible.
 */
class RegisterFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_register.xml
     */
    private var binding: FragmentRegisterBinding? = null

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
        val fragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_register.xml.
     * You can find these variables declared in the <data> section of fragment_register.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterFragment
            registerFragment = this@RegisterFragment
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [RegisterFragment] to screen readers when it is resumed and prefill edit text fields.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [RegisterFragment].
     * Announcing [RegisterFragment] when it is resumed increases the accessibility of the application.
     * Prefill edit text fields if there are already corresponding values stored in [userViewModel].
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER1)
        view?.announceForAccessibility(getString(R.string.register_accessibility_label))

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

    /**
     * Check whether all values provided by user are valid.
     *
     * Call [checkValues] to check values for validity. If all values are valid, jump to next [Fragment] which is [RegisterUserNameFragment].
     */
    fun goToNextView() {
        // before going t next fragment, check all values provided by user
        val valuesCorrect = checkValues()
        if (valuesCorrect) {
            findNavController().navigate(R.id.action_navigation_register1_to_navigation_register2)
        }
    }

    /**
     * Check whether all values provided by the user are valid.
     *
     * Call [checkEmailFormat]to check whether given email address is valid.
     * Check whether the other values are blank or not.
     * If so, show an error message in the corresponding edit text fields by calling [showErrors].
     * If all values are valid, store data in [userViewModel] by calling the setData method of [userViewModel].
     */
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

    /**
     * Checks whether [email] is a valid email address.
     *
     * This method will not check whether the email actually exists but it will check whether it is in right format.
     * First, check whether [email] is blank. After this, check the email format by matching it with [android.util.Patterns.EMAIL_ADDRESS].
     *
     * @param email email [String] provided by the user.
     * @return true if [email] matches the email format and false if not.
     */
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

    /**
     * Show errors in edit text fields.
     *
     * If one of the values [fnOk], [lnOk] or [emailOk] is false, this function wil be called to mark the corresponding edit text field with an error.
     *
     * @param fnOk Indicates whether firstname is valid.
     * @param lnOk Indicates whether lastname is valid.
     * @param emailOk Indicates whether email is valid.
     */
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