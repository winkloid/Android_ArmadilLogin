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
import de.tuchemnitz.armadillogin.ui.welcome.UserDataFragment

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
     * Binding object instance.
     *
     * Refers to fragment_user_overview.xml
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
     * The data is only used to make the registration process as genuine as possible by requesting data from the user that would also be requested during a real registration process.
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

    /**
     * Call super.onViewCreated() and setup data binding. Show the list of registered credentials for the user who is logged in.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_user_overview.xml.
     * You can find these variables declared in the <data> section of fragment_user_overview.xml.
     * Create a new instance of [CredentialAdapter] to show a recycler view of registered credentials for the user.
     * Setup observer to get credential list updates immediately.
     */
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
        // these lines are used as shown at https://github.com/googlecodelabs/fido2-codelab/blob/master/android/app/src/main/java/com/example/android/fido2/ui/home/HomeFragment.kt
        val credentialAdapter = CredentialAdapter(
            { credentialId ->
                DeleteConfirmationFragment.newInstance(credentialId)
                    .show(childFragmentManager, FRAGMENT_DELETE_CONFIRMATION)
            },
            viewLifecycleOwner,
            sharedViewModel
        )

        // setup recyclerView
        binding?.recyclerviewUserOverviewCredentials?.run {
            layoutManager = LinearLayoutManager(view.context)
            adapter = credentialAdapter
        }

        // setup observer to get instant updates of the CredentialList
        viewModel.credentialList.observe(viewLifecycleOwner) { credentialList ->
            credentialAdapter.submitList(credentialList)

            // if there are no more credentials in the list: show a corresponding message
            binding?.userOverviewCredentialPlaceholder?.visibility = if (credentialList.isEmpty()) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
        // End of lines as shown in linked github repository
    }

    /**
     * Delete keys.
     *
     * Is triggered when the [DeleteConfirmationFragment.Listener] receives a corresponding message from the AlertDialog of the [DeleteConfirmationFragment].
     * Calls the removeKey method of [viewModel].
     */
    override fun onDeleteConfirmed(credentialId: String) {
        viewModel.removeKey(credentialId)
    }

    /**
     * Set fragment status in [sharedViewModel] and announce [UserOverviewFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [UserOverviewFragment].
     * Announcing [UserOverviewFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.USER_OVERVIEW)
        view?.announceForAccessibility(getString(R.string.user_overview_accessibility_label))
    }

    /**
     * Send study data to database and jump to next fragment.
     *
     * Use sendData method of [studyUserViewModel] to send study data (age, gender, technical experience, needed time to complete study and registration and login process) to my study database.
     * When all data has been successfully sent, [findNavController]is called to get NavController and navigate to next [Fragment].
     */
    fun finishStudy() {
        studyUserViewModel.sendData()
        studyUserViewModel.sentStudyData.observe(viewLifecycleOwner) { sentStudyData ->
            if (sentStudyData) {
                findNavController().navigate(R.id.action_navigation_user_overview_to_navigation_finished)
            }
        }
    }
}