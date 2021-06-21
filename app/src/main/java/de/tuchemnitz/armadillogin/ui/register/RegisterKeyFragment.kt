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
import de.tuchemnitz.armadillogin.model.UserDataViewModel
import de.tuchemnitz.armadillogin.ui.observeOnce

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterKeyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterKeyFragment : Fragment() {

    companion object {
        const val FIDO2_REGISTER_REQUEST_CODE = 1
        private const val LOG_TAG = "FIDO2_REGISTERKEY"
    }

    private var binding: FragmentRegisterKeyBinding? = null
    private val sharedViewModel: ArmadilloViewModel by activityViewModels()
    private val userViewModel: UserDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterKeyBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@RegisterKeyFragment
            registerKeyFragment = this@RegisterKeyFragment
            userDataModel = userViewModel
            armadilloViewModel = sharedViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setFragmentStatus(FragmentStatus.REGISTER_KEY)
    }

    // sendRegisterRequest and onActivityResult are used as shown in https://github.com/googlecodelabs/fido2-codelab
    fun sendRegisterRequest() {
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

                    // inserted by winkloid - navigate to RegisterFinishedFragment after data transmission
                    userViewModel.registeringKey.observe(viewLifecycleOwner) { registeringKey ->
                        if (!registeringKey) {
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