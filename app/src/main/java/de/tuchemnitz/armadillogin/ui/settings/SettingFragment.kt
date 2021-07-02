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
import de.tuchemnitz.armadillogin.ui.welcome.UserDataFragment

/**
 * A [Fragment] subclass used as setting fragment.
 *
 * This class is used to display different settings. These settings can be changed using switch buttons or radio buttons.
 */
class SettingFragment : Fragment() {

    /**
     * Binding object instance. Refers to fragment_setting.xml
     */
    private var binding: FragmentSettingBinding? = null

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
        val fragmentBinding = FragmentSettingBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding. Implements dyslexic font setting.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_setting.xml.
     * You can find these variables declared in the <data> section of fragment_setting.xml.
     * In addition, a listener is set here that changes the value of dyslexicFont in [sharedViewModel] when the switch for the dyslexic font setting is pressed.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@SettingFragment
            settingFragment = this@SettingFragment
            armadilloViewModel = sharedViewModel
        }

        // dyslexic font switch setting
        binding?.switchSettingsChangeFont?.setOnCheckedChangeListener { _, checked ->
            sharedViewModel.setDyslexicFont(checked)
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [SettingFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [SettingFragment].
     * Announcing [SettingFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        view?.announceForAccessibility(getString(R.string.settings_accessibility_label))
    }
}