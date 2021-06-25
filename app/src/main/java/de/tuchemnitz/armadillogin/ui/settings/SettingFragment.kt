package de.tuchemnitz.armadillogin.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentSettingBinding
import de.tuchemnitz.armadillogin.databinding.FragmentWelcomeBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    private var binding: FragmentSettingBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSettingBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@SettingFragment
            settingFragment = this@SettingFragment
            armadilloViewModel = sharedViewModel
        }

        binding?.switchSettingsChangeFont?.setOnCheckedChangeListener { _, checked ->
            sharedViewModel.setDyslexicFont(checked)
        }
    }

    override fun onResume() {
        super.onResume()
        view?.announceForAccessibility(getString(R.string.settings_accessibility_label))
    }
}