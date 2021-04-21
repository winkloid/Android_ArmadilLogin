package de.tuchemnitz.armadillogin.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentLoginBinding
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            loginFragment = this@LoginFragment
        }
    }

    fun goToNextView() {
        findNavController().navigate(R.id.)
    }
}