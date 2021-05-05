package de.tuchemnitz.armadillogin.model

import android.app.Application
import android.app.PendingIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.fido.fido2.Fido2ApiClient
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterBinding
import de.tuchemnitz.armadillogin.fido2user.AuthRepository

class UserDataViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository.getInstance(application)

    private var _firstname = MutableLiveData<String>("")
    val firstname: LiveData<String> = _firstname

    private var _lastname = MutableLiveData<String>("")
    val lastname: LiveData<String> = _lastname

    private var _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private var _username = MutableLiveData("")
    val username: LiveData<String> = _username

    // server accepts every password with this server config. To show the real advantages of fido2, no password is asked from the users
    private val _password = MutableLiveData<String>("defaultpass")
    val password: LiveData<String> = _password

    private val _sendingUsername = MutableLiveData<Boolean>()
    val sendingUsername: LiveData<Boolean> = _sendingUsername

    private val _sendingPassword = MutableLiveData<Boolean>()
    val sendingPassword: LiveData<Boolean> = _sendingPassword

    private val _registeringKey = MutableLiveData<Boolean>()
    val registeringKey: LiveData<Boolean> = _registeringKey

    // used by MainActivity to create FIDO2 client
    fun setFido2ApiClient(fidoClient: Fido2ApiClient?) {
        repository.setFido2APiClient(fidoClient)
    }

    fun setData(fn: String, ln: String, email: String) {
        _firstname.value = fn
        _lastname.value = ln
        _email.value = email
    }

    fun setUsername(uname: String) {
        _username.value = uname
    }

    fun getFullName(): String {
        return "${firstname.value} ${lastname.value}"
    }

    fun sendUsername() {
        repository.username(username.value!!, _sendingUsername)
    }

    fun sendPassword() {
        repository.password(password.value!!, _sendingPassword)
    }

    fun registerRequest() : LiveData<PendingIntent?> {
        return repository.registerRequest(_registeringKey)
    }

}