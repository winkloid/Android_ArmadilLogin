package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterFinishedBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.StudyUserDataViewModel
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A [Fragment] subclass used as register finished fragment.
 *
 * In this fragment, the user is informed that the registration process was completed successfully.
 * The user gets the possibility to go back to [de.tuchemnitz.armadillogin.ui.registerlogin.RegisterLoginFragment] to start the login process from there.
 */
class RegisterFinishedFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_register_finished.xml
     */
    private var binding: FragmentRegisterFinishedBinding? = null

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
        val fragmentBinding = FragmentRegisterFinishedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [RegisterFinishedFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [RegisterFinishedFragment].
     * Announcing [RegisterFinishedFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_FINISHED)
        view?.announceForAccessibility(getString(R.string.register_finished_accessibility_label))
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_register_finished.xml.
     * You can find these variables declared in the <data> section of fragment_register_finished.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterFinishedFragment
            registerFinishedFragment = this@RegisterFinishedFragment
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Delete all personal data and jump back to register login fragment.
     *
     * Delete personal data of the user (firstname, lastname, email) and sign out the user by calling signOutOnError method from [userViewModel].
     */
    fun backToStart() {
        userViewModel.signOutOnErrorOrFinished()
        findNavController().navigate(R.id.action_navigation_register_finished_to_navigation_register_login)
    }
}