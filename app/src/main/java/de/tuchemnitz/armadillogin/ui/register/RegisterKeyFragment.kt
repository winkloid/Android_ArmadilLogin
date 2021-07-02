package de.tuchemnitz.armadillogin.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.api.common.AuthenticatorErrorResponse
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterKeyBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.StudyUserDataViewModel
import de.tuchemnitz.armadillogin.model.UserDataViewModel
import de.tuchemnitz.armadillogin.ui.observeOnce

/**
 * A [Fragment] subclass used as register username fragment.
 *
 * In this [Fragment], the user is informed about the process of registering his key in the app in order to log in with it again later.
 * There is also a button that starts the registration process of the FIDO2 key.
 */
class RegisterKeyFragment : Fragment() {

    companion object {
        const val FIDO2_REGISTER_REQUEST_CODE = 1
        private const val LOG_TAG = "FIDO2_REGISTERKEY"
    }

    /**
     * Binding object instance.
     *
     * Refers to fragment_register_key.xml
     */
    private var binding: FragmentRegisterKeyBinding? = null

    /**
     * Shared [ArmadilloViewModel] for fragment status and settings.
     *
     * Manages the [FragmentStatus] value to display adapted resources in the Help tab. It also manages some variables to store user settings like color mode or font settings.
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
     * Shared [StudyUserDataViewModel] for study data.
     *
     * Saves all data provided by users and time data. It also provides functionality to securely transmit these data to my study database.
     */
    private val studyUserDataViewModel: StudyUserDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterKeyBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_register_key.xml.
     * You can find these variables declared in the <data> section of fragment_register_key.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterKeyFragment
            registerKeyFragment = this@RegisterKeyFragment
            userDataModel = userViewModel
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [RegisterKeyFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [RegisterKeyFragment].
     * Announcing [RegisterKeyFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_KEY)
        view?.announceForAccessibility(getString(R.string.register_key_accessibility_label))
    }

    // sendRegisterRequest and onActivityResult are used as shown in https://github.com/googlecodelabs/fido2-codelab
    /**
     * Send a request to server to register FIDO2 key. Measure system time.
     *
     * Measure the system time to get beginning time of registration intent.
     * Send a register request to FIDO2 server by calling registerRequest method of [userViewModel] which returns a PendingIntent as LiveData.
     * Set observer to this method and start registration intent as soon as response comes from the server.
     */
    fun sendRegisterRequest() {
        if (studyUserDataViewModel.userRegisterStartTime == null) {
            studyUserDataViewModel.userRegisterStartTime = System.nanoTime()
        }
        userViewModel.registerRequest().observeOnce(requireActivity()) { pendingIntent ->
            startIntentSenderForResult(
                pendingIntent.getIntentSender(),
                FIDO2_REGISTER_REQUEST_CODE,
                null,
                0,
                0,
                0,
                null
            )
        }
    }

    /**
     * Get result from registration intent and jump to [RegisterErrorFragment] or [RegisterFinishedFragment].
     *
     * If the register response contains errors, jump to [RegisterErrorFragment].
     * If the response signals that the intent was canceled, show a corresponding toast message.
     * If the response shows no errors, jump to [RegisterFinishedFragment].
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FIDO2_REGISTER_REQUEST_CODE) {
            val errorExtra = data?.getByteArrayExtra(Fido.FIDO2_KEY_ERROR_EXTRA)
            when {
                errorExtra != null -> {
                    val error = AuthenticatorErrorResponse.deserializeFromBytes(errorExtra)
                    error.errorMessage?.let { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                        Log.e(LOG_TAG, errorMessage)
                    }

                    // inserted by winkloid - navigate to RegisterErrorFragment when error occured while registering key
                    findNavController().navigate(R.id.action_navigation_register_key_to_navigation_register_error)
                }
                resultCode != Activity.RESULT_OK -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.register_key_cancelled,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                data != null -> {
                    userViewModel.registerResponse(data)

                    // inserted by winkloid - navigate to RegisterFinishedFragment after data transmission and store register finished time
                    userViewModel.registeringKey.observe(viewLifecycleOwner) { registeringKey ->
                        if (!registeringKey) {
                            if (studyUserDataViewModel.userRegisterFinishedTime == null) {
                                studyUserDataViewModel.userRegisterFinishedTime = System.nanoTime()
                            }
                            findNavController().navigate(R.id.action_navigation_register_key_to_navigation_register_finished)
                        }
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}