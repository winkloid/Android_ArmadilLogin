package de.tuchemnitz.armadillogin.ui.registerlogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentHelpBinding
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterLoginBinding
import de.tuchemnitz.armadillogin.ui.help.HelpViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterLoginFragment : Fragment() {

    private var binding: FragmentRegisterLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            registerLoginFragment = this@RegisterLoginFragment
        }
    }
}