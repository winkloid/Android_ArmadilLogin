package de.tuchemnitz.armadillogin.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterKeyBinding
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterSummaryBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterKeyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterKeyFragment : Fragment() {
    private var binding: FragmentRegisterKeyBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val userViewModel: UserDataViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterKeyBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterKeyFragment
            registerKeyFragment = this@RegisterKeyFragment
            userDataModel = userViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_KEY)
    }


}