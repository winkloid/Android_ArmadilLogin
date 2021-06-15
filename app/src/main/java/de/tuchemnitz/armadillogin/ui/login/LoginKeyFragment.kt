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
import de.tuchemnitz.armadillogin.model.UserDataViewModel
import de.tuchemnitz.armadillogin.ui.observeOnce

/**
 * A simple [Fragment] subclass.
 * Use the [LoginKeyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginKeyFragment : Fragment() {

    companion object {
        const val FIDO2_SIGNIN_REQUEST_CODE = 2
        private const val LOG_TAG = "FIDO2_REGISTERKEY"
    }

    private var binding: FragmentLoginKeyBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val userViewModel: UserDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginKeyBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@LoginKeyFragment
            loginKeyFragment = this@LoginKeyFragment
            userDataModel = userViewModel
            armadilloViewModel = sharedViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.LOGIN_KEY)
    }

    // sendLoginRequest and onActivityResult are used as shown in https://github.com/googlecodelabs/fido2-codelab
    fun sendLoginRequest() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(LOG_TAG, "requestCode: $requestCode")

        if(requestCode == FIDO2_SIGNIN_REQUEST_CODE) {
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
                    Toast.makeText(requireContext(), R.string.signin_key_cancelled, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if(data!= null) {
                        userViewModel.signInResponse(data)
                        Log.d(LOG_TAG, "$data")

                        // inserted by winkloid
                        // observe whether signInResponse already completed and suceeded and if so, go to next fragment
                        userViewModel.signInReady.observe(viewLifecycleOwner) { signInReady ->
                            if (signInReady) {
                                userViewModel.userFinishedTime = System.nanoTime()
                                userViewModel.calculateUserTime()
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