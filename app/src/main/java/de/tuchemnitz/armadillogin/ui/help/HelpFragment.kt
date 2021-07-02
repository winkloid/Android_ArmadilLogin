package de.tuchemnitz.armadillogin.ui.help

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentHelpBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.ui.login.LoginFragment

/**
 * A [Fragment] subclass used as help fragment.
 *
 * In this [Fragment] all help resources that are relevant for the current fragment in the Login tab are displayed. This is realized in a RecylcerView.
 */
class HelpFragment : Fragment() {

    /**
     * Binding object instance.
     *
     * Refers to fragment_help.xml
     */
    private var binding: FragmentHelpBinding? = null

    /**
     * Local [HelpViewModel].
     *
     * Instantiates [HelpDataXmlParser], converts /assets/helpdata.xml to inputStream, parses it and saves the parsed data.
     * It also has functionality to convert help resource name [String]s to real help resource IDs.
     */
    private lateinit var helpViewModel: HelpViewModel

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
        val fragmentBinding = FragmentHelpBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_help.xml.
     * You can find these variables declared in the <data> section of fragment_help.xml.
     */
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpViewModel = ViewModelProvider(this).get(HelpViewModel::class.java)

        binding?.apply {
            helpFragment = this@HelpFragment
            lifecycleOwner = this@HelpFragment
            viewModel = sharedViewModel
            helpModel = helpViewModel
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Announce [LoginFragment] to screen readers when it is resumed.
     *
     * Announcing [LoginFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        view?.announceForAccessibility(getString(R.string.help_accessibility_label))
    }

    /**
     * Get the application context from other classes.
     *
     * This is useful to retrieve resources which are linked to the application.
     */
    fun getHelpContext(): FragmentActivity? {
        return activity
    }

    /**
     * Get [LifecycleOwner] of this [Fragment] from other classes.
     *
     * This can be useful to use observers with this [LifecycleOwner] in other classes.
     */
    fun getLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }
}