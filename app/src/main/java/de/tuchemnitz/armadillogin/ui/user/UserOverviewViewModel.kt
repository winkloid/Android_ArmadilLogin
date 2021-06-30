package de.tuchemnitz.armadillogin.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.tuchemnitz.armadillogin.fido2user.AuthRepository

/**
 * ViewModel for retrieving and deleting keys of a user.
 *
 * Is used in UserOverviewFragment for key management.
 */
class UserOverviewViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Instance of [AuthRepository].
     *
     * Will be used to contact Authentication API.
     * Includes all methods that are useful for FIDO2 registration, login, adding, retrieving or deleting keys, etc.
     */
    private val repository = AuthRepository.getInstance(application)

    /**
     * List of all registered FIDO2 keys of a user.
     */
    val credentialList = repository.getCredentials()

    /**
     * Represents whether a delete operation is still in progress or not.
     *
     * While the value is false, there is no ongoing delete operation.
     */
    private var _removingKey = MutableLiveData(false)
    val removingKey: LiveData<Boolean> = _removingKey

    /**
     * Remove Key.
     *
     * Contact the removeKey method of [AuthRepository] class to delete a key.
     * It is possible to delete all keys so that no keys are left. In this case a corresponding message is displayed in [UserOverviewFragment] to inform the user that there are no keys registered in his or her account.
     */
    fun removeKey(credentialId: String) {
        repository.removeKey(credentialId, _removingKey)
    }
}