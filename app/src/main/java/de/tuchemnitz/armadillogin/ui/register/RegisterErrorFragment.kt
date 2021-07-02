package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterErrorBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A [Fragment] subclass used as register error fragment.
 *
 * In this fragment, the user is informed that the registration process failed.
 * The user gets the possibility to go back to [de.tuchemnitz.armadillogin.ui.registerlogin.RegisterLoginFragment] to start the registration process once again or he/she can try registering a FIDO2 key again.
 * If the user decides to try registering a key again, the application will jump back to [RegisterKeyFragment].
 */
class RegisterErrorFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_register_error.xml
     */
    private var binding: FragmentRegisterErrorBinding? = null

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
        val fragmentBinding = FragmentRegisterErrorBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [RegisterErrorFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [RegisterErrorFragment].
     * Announcing [RegisterErrorFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_ERROR)
        view?.announceForAccessibility(getString(R.string.register_error_accessibility_label))
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_register_error.xml.
     * You can find these variables declared in the <data> section of fragment_register_error.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterErrorFragment
            registerErrorFragment = this@RegisterErrorFragment
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Delete personal data and jump back to register login fragment.
     *
     * Delete firstname, lastname, email and sign out user by calling signOutOnErrorOrFinished method in [userViewModel].
     * Navigate to register login fragment using [findNavController]
     */
    fun backToStart() {
        userViewModel.signOutOnErrorOrFinished()
        findNavController().navigate(R.id.action_navigation_register_error_to_navigation_register_login)
    }

    /**
     * Jump back to [RegisterKeyFragment].
     *
     * If the user decides to try registering a FIDO2 key once again, go back to the previous [Fragment] which was [RegisterKeyFragment].
     */
    fun backToPrevious() {
        activity?.onBackPressed()
    }
}