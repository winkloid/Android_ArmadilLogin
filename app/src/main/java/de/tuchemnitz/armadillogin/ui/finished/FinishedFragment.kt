package de.tuchemnitz.armadillogin.ui.finished

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentFinishedBinding
import de.tuchemnitz.armadillogin.databinding.FragmentWelcomeBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.ui.login.LoginFragment

/**
 * A [Fragment] subclass used as finished fragment.
 *
 * In this fragment, the user is informed that he/she has successfully completed the study.
 * In addition, the user is given the option to return to the [WelcomeFragment][de.tuchemnitz.armadillogin.ui.welcome.WelcomeFragment] via a button.
 */
class FinishedFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_finished.xml
     */
    private var binding: FragmentFinishedBinding? = null

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
        val fragmentBinding = FragmentFinishedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_finished.xml.
     * You can find these variables declared in the <data> section of fragment_finished.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@FinishedFragment
            finishedFragment = this@FinishedFragment
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [FinishedFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [FinishedFragment].
     * Announcing [FinishedFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.FINISHED)
        view?.announceForAccessibility(getString(R.string.finished_accessibility_label))
    }

    /**
     * Jump back to [WelcomeFragment][de.tuchemnitz.armadillogin.ui.welcome.WelcomeFragment].
     *
     * Call [findNavController] to navigate to [WelcomeFragment][de.tuchemnitz.armadillogin.ui.welcome.WelcomeFragment].
     */
    fun backToWelcome() {
        findNavController().navigate(R.id.action_navigation_finished_to_navigation_welcome)
    }
}