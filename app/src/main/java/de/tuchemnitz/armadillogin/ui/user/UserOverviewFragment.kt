package de.tuchemnitz.armadillogin.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentUserOverviewBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.StudyUserDataViewModel
import de.tuchemnitz.armadillogin.model.UserDataViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [UserOverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserOverviewFragment : Fragment(), DeleteConfirmationFragment.Listener {

    companion object {
        private const val LOG_TAG = "UserOverviewFragment"
        private const val FRAGMENT_DELETE_CONFIRMATION = "delete_confirmation"
    }

    private var binding: FragmentUserOverviewBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val userViewModel: UserDataViewModel by activityViewModels()
    private val viewModel: UserOverviewViewModel by activityViewModels()
    private val studyUserViewModel: StudyUserDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentUserOverviewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@UserOverviewFragment
            userOverviewFragment = this@UserOverviewFragment
            userDataModel = userViewModel
            viewModel = viewModel
            armadilloViewModel = sharedViewModel
            studyUserModel = studyUserViewModel
        }

        // credentials recyclerview binding
        // these lines are used as shown in https://github.com/googlecodelabs/fido2-codelab in HomeFragment
        val credentialAdapter = CredentialAdapter(
            { credentialId ->
                DeleteConfirmationFragment.newInstance(credentialId)
                    .show(childFragmentManager, FRAGMENT_DELETE_CONFIRMATION)
            },
            viewLifecycleOwner,
            sharedViewModel
        )
        binding?.recyclerviewUserOverviewCredentials?.run {
            layoutManager = LinearLayoutManager(view.context)
            adapter = credentialAdapter
        }
        viewModel.credentialList.observe(viewLifecycleOwner) { credentialList ->
            credentialAdapter.submitList(credentialList)
            Log.d(LOG_TAG, "$credentialList")
            binding?.userOverviewCredentialPlaceholder?.visibility = if (credentialList.isEmpty()) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    override fun onDeleteConfirmed(credentialId: String) {
        viewModel.removeKey(credentialId)
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.USER_OVERVIEW)
        view?.announceForAccessibility(getString(R.string.user_overview_accessibility_label))
    }

    fun finishStudy() {
        studyUserViewModel.sendData()
        studyUserViewModel.sentStudyData.observe(viewLifecycleOwner) { sentStudyData ->
            if (sentStudyData) {
                findNavController().navigate(R.id.action_navigation_user_overview_to_navigation_finished)
            }
        }
    }
}