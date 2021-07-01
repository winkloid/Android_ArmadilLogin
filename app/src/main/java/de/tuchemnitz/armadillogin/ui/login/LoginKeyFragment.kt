package de.tuchemnitz.armadillogin.ui.login

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
import de.tuchemnitz.armadillogin.databinding.FragmentLoginKeyBinding
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.StudyUserDataViewModel
import de.tuchemnitz.armadillogin.model.UserDataViewModel
import de.tuchemnitz.armadillogin.ui.observeOnce
import de.tuchemnitz.armadillogin.ui.register.RegisterErrorFragment
import de.tuchemnitz.armadillogin.ui.register.RegisterKeyFragment

/**
 * A [Fragment] subclass used as login key fragment.
 *
 * In this fragment, the user is informed about how the login process works with his FIDO2 key.
 * This is barely different from the registration of the key.
 * There is also a button in this fragment to start the login process with the FIDO2 key.
 */
class LoginKeyFragment : Fragment() {

    companion object {
        const val FIDO2_SIGNIN_REQUEST_CODE = 2
        private const val LOG_TAG = "FIDO2_REGISTERKEY"
    }

    /**
     * Binding object instance.
     *
     * Refers to fragment_login_key.xml
     */
    private var binding: FragmentLoginKeyBinding? = null

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
    private val studyUserViewModel: StudyUserDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginKeyBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    /**
     * Call super.onViewCreated() and activate data binding.
     *
     * Use this [Fragment] subclass as lifecycleOwner for data binding and assign the other variables to use them in fragment_login_key.xml.
     * You can find these variables declared in the <data> section of fragment_login_key.xml.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@LoginKeyFragment
            loginKeyFragment = this@LoginKeyFragment
            userDataModel = userViewModel
            armadilloViewModel = sharedViewModel
        }
    }

    /**
     * Set [FragmentStatus] in [sharedViewModel] and announce [LoginKeyFragment] to screen readers when it is resumed.
     *
     * Changing the fragment status is necessary to display content in help section that is adapted to [LoginKeyFragment].
     * Announcing [LoginKeyFragment] when it is resumed increases the accessibility of the application.
     */
    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.LOGIN_KEY)
        view?.announceForAccessibility(getString(R.string.login_key_accessibility_label))
    }

    // sendLoginRequest and onActivityResult are used as shown in https://github.com/googlecodelabs/fido2-codelab
    /**
     * Send a request to server to login with FIDO2 key. Measure system time.
     *
     * Measure the system time to get beginning time of login intent.
     * Send a signIn request to FIDO2 server by calling signInRequest method of [userViewModel] which returns a PendingIntent as LiveData.
     * Set observer to this method and start login intent as soon as response comes from the server.
     */
    fun sendLoginRequest() {
        if (studyUserViewModel.userLoginStartTime == null) {
            studyUserViewModel.userLoginStartTime = System.nanoTime()
        }
        userViewModel.signInRequest().observeOnce(this) { pendingIntent ->
            startIntentSenderForResult(
                pendingIntent.getIntentSender(),
                FIDO2_SIGNIN_REQUEST_CODE,
                null,
                0,
                0,
                0,
                null
            )
        }
    }

    /**
     * Get result from login intent and show corresponding messages or jump to next [Fragment]
     * If the [resultCode] contains an error, show a [Toast] message that contains the error message.
     * If the response signals that the intent was canceled, show a corresponding toast message.
     * If the response does not contain any errors, measure the [System] time so that userFinishedTime can be stored in [studyUserViewModel]. After this, jump to  [de.tuchemnitz.armadillogin.ui.user.UserOverviewFragment].
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == FIDO2_SIGNIN_REQUEST_CODE) {
            val errorExtra = data?.getByteArrayExtra(Fido.FIDO2_KEY_ERROR_EXTRA)

            when {
                (errorExtra != null) -> {
                    val error = AuthenticatorErrorResponse.deserializeFromBytes(errorExtra)
                    error.errorMessage?.let { errorMessage ->
                        // show Toast with errorMessage - comment by winkloid
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        Log.e(LOG_TAG, errorMessage)
                    }
                }
                (resultCode != Activity.RESULT_OK) -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.signin_key_cancelled,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    if (data != null) {
                        userViewModel.signInResponse(data)

                        // inserted by winkloid
                        // observe whether signInResponse already completed and suceeded and if so, go to next fragment
                        userViewModel.signInReady.observe(viewLifecycleOwner) { signInReady ->
                            if (signInReady) {
                                if(studyUserViewModel.userFinishedTime == null) {
                                    studyUserViewModel.userFinishedTime = System.nanoTime()
                                }
                                studyUserViewModel.calculateUserTime()
                                findNavController().navigate(R.id.action_navigation_login_key_to_navigation_user_overview)
                                userViewModel.setSignInKey()
                            }
                        }
                    }
                }
            }
        }
    }
}