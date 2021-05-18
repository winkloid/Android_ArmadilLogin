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
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentHelpBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel

class HelpFragment : Fragment() {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private var binding: FragmentHelpBinding? = null
    private lateinit var helpViewModel: HelpViewModel
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentBinding = FragmentHelpBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

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

    fun getHelpContext() : FragmentActivity? {
        return activity
    }
}