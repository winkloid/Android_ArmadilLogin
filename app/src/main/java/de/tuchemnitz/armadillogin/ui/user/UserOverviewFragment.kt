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
 * A [Fragment] and [DeleteConfirmationFragment.Listener] subclass used as user overview fragment.
 *
 * This class is used to display the times the user needed to complete the login and registration process.
 * The user is also able to finish the study and send his or her user data to my study database by pressing a button.
 * An overview of all registered FIDO2 keys is also displayed.
 */
class UserOverviewFragment : Fragment(), DeleteConfirmationFragment.Listener {

    companion object {
        private const val LOG_TAG = "UserOverviewFragment"
        private const val FRAGMENT_DELETE_CONFIRMATION = "delete_confirmation"
    }

    /**
     * Binding object instance. Refers to fragment_user_data.xml
     */
    private var binding: FragmentUserOverviewBinding? = null

    /**
     * Shared [ArmadilloViewModel] for fragment status and settings.
     *
     * Manages the current fragment to display adapted resources in the Help tab. It also manages some variables to store user settings like color mode or font settings.
     */
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()

    /**
     * Shared [UserDataViewModel] for storing personal data during registration process.
     *
     * This data is not stored after the app is closed, nor does it serve the purpose of data collection at all.
     * They are only used to make the registration process as genuine as possible by requesting data from the user that would also be requested during a real registration process.
     * This data includes, among other things, name, e-mail address and username.
     * In addition, the ViewModel interfaces with the FIDO2 API.
     */
    private val userViewModel: UserDataViewModel by activityViewModels()

    /**
     * [UserOverviewViewModel] for retrieving and deleting keys of the user who is logged in.
     */
    private val viewModel: UserOverviewViewModel by activityViewModels()

    /**
     * Shared [StudyUserDataViewModel] for study data.
     *
     * Saves all data provided by users and time data. It also provides functionality to securely transmit these data to my study database.
     */
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

        /**
         * Use data binding.
         *
         * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_user_overview.xml.
         * You can find these variables declared in the <data> section of fragment_user_overview.xml.
         */
        binding?.apply {
            lifecycleOwner = this@UserOverviewFragment
            userOverviewFragment = this@UserOverviewFragment
            userDataModel = userViewModel
            viewModel = viewModel
            armadilloViewModel = sharedViewModel
            studyUserModel = studyUserViewModel
        }

        // credentials recyclerview binding
        // these lines are used as shown at https://github.com/googlecodelabs/fido2-codelab in HomeFragment
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