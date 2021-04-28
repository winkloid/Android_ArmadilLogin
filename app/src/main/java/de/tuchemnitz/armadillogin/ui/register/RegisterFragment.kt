package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterBinding
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterLoginBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

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
            registerFragment = this@RegisterFragment
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER1)
    }

    fun goToNextView() {
        // before going t next fragment, check all values provided by user
        checkValues()
        findNavController().navigate(R.id.action_navigation_register1_to_navigation_register2)
    }

    private fun checkValues() {
        val lastname = binding?.registerInputLastnameEditText?.text.toString()
        val firstname = binding?.registerInputFirstnameEditText?.text.toString()
        val email = binding?.registerInputEmailEditText?.text.toString()
        if (!firstname.isNullOrBlank()) {
            Log.d("FIRSTNAME", "$firstname")
        }
        if(!lastname.isNullOrBlank()) {
            Log.d("Lastname", "$lastname")
        }
        if(!email.isNullOrBlank()) {
            Log.d("EMAIL", "$email")
        }
    }
}