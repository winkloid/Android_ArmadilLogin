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
import de.tuchemnitz.armadillogin.databinding.FragmentUserDataBinding
import de.tuchemnitz.armadillogin.databinding.FragmentWelcomeBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.StudyUserDataViewModel
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A [Fragment] subclass used as user data fragment.
 *
 * This class is used to ask the user for several personal data (such as age, gender, technical experience) which could be potentially useful for my study. The provision of all data is voluntary.
 */
class UserDataFragment : Fragment() {

    /**
     * Binding object instance. Refers to fragment_user_data.xml
     */
    private var binding: FragmentUserDataBinding? = null

    /**
     * Shared [ArmadilloViewModel] for fragment status and settings.
     *
     * Manages the [FragmentStatus] value to display adapted resources in the Help tab. It also manages some variables to store user settings like color mode or font settings.
     */
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    /**
     * Shared [StudyUserDataViewModel] for study data.
     *
     * Saves all data provided by users and time data. It also provides functionality to securely transmit these data to my study database.
     */
    private val studyUserViewModel: StudyUserDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentUserDataBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_user_data.xml.
     * You can find these variables declared in the <data> section of fragment_user_data.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@UserDataFragment
            userDataFragment = this@UserDataFragment
            armadilloViewModel = sharedViewModel
            studyUserModel = studyUserViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [UserDataFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [UserDataFragment].
     * Announcing [UserDataFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()

        // Set fragment status in sharedViewModel to user data fragment.
        sharedViewModel.setFragmentStatus(FragmentStatus.USER_DATA)

        // Announce UserDataFragment to screen readers when it is resumed.
        view?.announceForAccessibility(getString(R.string.user_data_accessibility_label))
    }

    /**
     * Check provided values and jump to RegisterLoginFragment.
     *
     * If the button in [UserDataFragment] is clicked, [checkValues] is called and if this returns true, system time is captured using [System.nanoTime] and, after this, [findNavController] is called to navigate to next fragment.
     */
    fun goToNextView() {
        val valuesCorrect = checkValues()
        if (valuesCorrect) {
            // only capture and store system time if it has not been captured before
            if(studyUserViewModel.userStartTime == null) {
                studyUserViewModel.userStartTime = System.nanoTime()
            }
            findNavController().navigate(R.id.action_navigation_user_data_to_navigation_register_login)
        }
    }

    /**
     * Checks whether the edit text field for the user's age contains numbers only.
     *
     * If the user has only entered numbers in the edit text field, the specified age will be saved using the [studyUserViewModel.setAge] method.
     * Otherwise an error is displayed and the method returns false.
     *
     * @return true if provided data in age edit text field are numbers only and false if the user provided also non-numeric characters.
     */
    private fun checkValues(): Boolean {
        val ageInput = binding?.userDataInputAgeEditText?.text
        if (ageInput.isNullOrBlank()) {
            studyUserViewModel.setAge(null)
            return true
        } else {
            val age = ageInput.toString().toIntOrNull()
            if (age != null) {
                studyUserViewModel.setAge(age)
                return true
            } else {
                binding?.userDataInputAgeEditText?.error =
                    getString(R.string.user_data_no_age_error)
                return false
            }
        }
    }
}