package de.tuchemnitz.armadillogin.ui.help

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.tuchemnitz.armadillogin.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private var binding: FragmentHelpBinding? = null
    private lateinit var helpViewModel: HelpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentBinding = FragmentHelpBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helpViewModel = ViewModelProvider(this).get(HelpViewModel::class.java)
        binding?.apply {
            helpFragment = this@HelpFragment
        }
    }

}