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
 * A [Fragment] subclass used as register summary fragment.
 *
 * In this [Fragment], the user is shown all the data he entered once again. Then the user has the possibility to correct or confirm his/her data.
 * If he/she decides to correct the data, he/she will return to the Register1 fragment.
 * If he/she confirms the information, a connection is established to the FIDO2 server to register the user name.
 */
class RegisterSummaryFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_register_summary.xml
     */
    private var binding: FragmentRegisterSummaryBinding? = null

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
        val fragmentBinding = FragmentRegisterSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_register_summary.xml.
     * You can find these variables declared in the <data> section of fragment_register_summary.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterSummaryFragment
            registerSummaryFragment = this@RegisterSummaryFragment
            userDataModel = userViewModel
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [RegisterSummaryFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [RegisterSummaryFragment].
     * Announcing [RegisterSummaryFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_SUMMARY)
        view?.announceForAccessibility(getString(R.string.register_summary_accessibility_label))
    }

    /**
     * Jump Back to [RegisterFragment].
     *
     * If the user clicks the "Correct" button, go back to [RegisterFragment] to let him/her correct the input values.
     */
    fun goBackToRegister() {
        findNavController().navigate(R.id.action_navigation_register_summary_to_navigation_register1)
    }

    /**
     * Jump to next [Fragment]: [RegisterKeyFragment]
     *
     * If the "confirm" button is clicked, send username and default password to FIDO2 server and wait for completion of the action.
     * After this go to the next [Fragment] which will be [RegisterKeyFragment]
     */
    fun goToNextFragment() {
        Toast.makeText(
            activity,
            getString(R.string.register_summary_sending_username),
            Toast.LENGTH_SHORT
        ).show()

        // wait for username and password to be sent and then go to the next fragment
        // make sure to send username before password
        userViewModel.sendUsername()
        userViewModel.usernameBeforePassword.observe(viewLifecycleOwner) { usernameBeforePassword ->
            if (usernameBeforePassword) {
                userViewModel.sendPassword()
                userViewModel.passwordBeforeNextTask.observe(viewLifecycleOwner) { passwordBeforeNextTask ->
                    if (passwordBeforeNextTask) {
                        findNavController().navigate(R.id.action_navigation_register_summary_to_navigation_register_key)
                        userViewModel.setPasswordBeforeNextTask()
                    }
                }
            }
        }
    }
}