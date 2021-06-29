package de.tuchemnitz.armadillogin.ui.welcome

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentWelcomeBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.StudyUserDataViewModel
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A [Fragment] subclass used as welcome fragment.
 *
 * This class should briefly inform the user about the purpose of the app. It only shows a short information text and a button to continue.
 */
class WelcomeFragment : Fragment() {

    /**
     * Binding object instance. Refers to fragment_welcome.xml
     */
    private var binding: FragmentWelcomeBinding? = null

    /**
     * Shared [ArmadilloViewModel] for fragment status and settings.
     *
     * Manages the current fragment to display adapted resources in the Help tab. It also manages some variables to store user settings like color mode or font settings.
     */
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Use data binding.
         *
         * Use this fragment subclass as lifecycleOwner for data binding and assign the other two variables to use them in fragment_welcome.xml.
         * You can find these variables declared in the <data> section of fragment_welcome.xml.
         */
        binding?.apply {
            lifecycleOwner = this@WelcomeFragment
            welcomeFragment = this@WelcomeFragment
            armadilloViewModel = sharedViewModel
        }
    }

    override fun onResume() {
        super.onResume()

        /**
         * Set fragment status in [sharedViewModel] to welcome fragment.
         *
         * This is necessary to display content in help section that is adapted to [UserDataFragment].
         */
        sharedViewModel.setFragmentStatus(FragmentStatus.WELCOME)

        /**
         * Announce [WelcomeFragment] to screen readers when it is resumed.
         */
        view?.announceForAccessibility(getString(R.string.welcome_accessibility_label))
    }

    /**
     * Jump to UserDataFragment.
     *
     * If the button in [WelcomeFragment] is clicked, [findNavController] is called to navigate to next fragment.
     */
    fun goToNextView() {
        findNavController().navigate(R.id.action_navigation_welcome_to_navigation_user_data)
    }
}