package de.tuchemnitz.armadillogin.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.tuchemnitz.armadillogin.fido2user.AuthRepository

class UserOverviewViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository.getInstance(application)

    val credentialList = repository.getCredentials()

    private var _removingKey = MutableLiveData(false)
    val removingKey: LiveData<Boolean> = _removingKey


    fun removeKey(credentialId: String) {
        repository.removeKey(credentialId, _removingKey)
    }
}