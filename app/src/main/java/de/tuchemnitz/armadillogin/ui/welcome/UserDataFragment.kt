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
 * A simple [Fragment] subclass.
 * Use the [UserDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserDataFragment : Fragment() {
    private var binding: FragmentUserDataBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val studyUserViewModel: StudyUserDataViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentBinding = FragmentUserDataBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@UserDataFragment
            userDataFragment = this@UserDataFragment
            armadilloViewModel = sharedViewModel
            studyUserModel = studyUserViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("D","Resuming")
        sharedViewModel.setFragmentStatus(FragmentStatus.USER_DATA)
    }
    fun goToNextView() {
        val valuesCorrect = checkValues()
        if (valuesCorrect) {
            studyUserViewModel.userStartTime = System.nanoTime()
            findNavController().navigate(R.id.action_navigation_user_data_to_navigation_register_login)
        }
    }

    private fun checkValues(): Boolean {
        val ageInput = binding?.userDataInputAgeEditText?.text
        if(ageInput.isNullOrBlank()) {
            studyUserViewModel.setAge(null)
            return true
        } else {
            val age = ageInput.toString().toIntOrNull()
            if(age != null) {
                studyUserViewModel.setAge(age)
                return true
            } else {
                binding?.userDataInputAgeEditText?.error = getString(R.string.user_data_no_age_error)
                return false
            }
        }
    }
}