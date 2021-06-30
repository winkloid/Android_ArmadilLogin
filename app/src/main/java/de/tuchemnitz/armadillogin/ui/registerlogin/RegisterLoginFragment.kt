package de.tuchemnitz.armadillogin.ui.registerlogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentHelpBinding
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterLoginBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.ui.help.HelpViewModel
import de.tuchemnitz.armadillogin.ui.settings.SettingFragment

/**
 * A [Fragment] subclass used as register login fragment.
 *
 * This fragment offers the user two possible ways to proceed: either the user can log in or go through the registration process first, if he/she does not have an account yet.
 */
class RegisterLoginFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_register_login.xml
     */
    private var binding: FragmentRegisterLoginBinding? = null

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
        val fragmentBinding = FragmentRegisterLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_register_login.xml.
     * You can find these variables declared in the <data> section of fragment_register_login.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterLoginFragment
            registerLoginFragment = this@RegisterLoginFragment
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [RegisterLoginFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [RegisterLoginFragment].
     * Announcing [RegisterLoginFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_LOGIN)
        view?.announceForAccessibility(getString(R.string.register_login_accessibility_label))
    }

    /**
     * If user decides to register, jump to register1 fragment.
     *
     * Call [findNavController] to navigate to register1 fragment.
     */
    fun goToRegisterFragment() {
        findNavController().navigate(R.id.action_navigation_register_login_to_navigation_register1)
    }

    /**
     * If user decides to register, jump to login fragment.
     *
     * Call [findNavController] to navigate to login fragment.
     */
    fun goToLoginFragment() {
        findNavController().navigate(R.id.action_navigation_register_login_to_navigation_login1)
    }
}