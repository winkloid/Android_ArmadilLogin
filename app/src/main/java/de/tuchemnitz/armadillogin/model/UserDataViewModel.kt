package de.tuchemnitz.armadillogin.model

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.fido.fido2.Fido2ApiClient
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
    private val password: LiveData<String> = _password

    // bools to show animations or adapt UI when user has to wait until a task is finished
    private val _sendingUsername = MutableLiveData<Boolean>()
    val sendingUsername: LiveData<Boolean> = _sendingUsername

    private val _sendingPassword = MutableLiveData<Boolean>()
    val sendingPassword: LiveData<Boolean> = _sendingPassword

    private val _registeringKey = MutableLiveData<Boolean>()
    val registeringKey: LiveData<Boolean> = _registeringKey

    private val _signInKey = MutableLiveData<Boolean>()
    val signInKey: LiveData<Boolean> = _signInKey

    // used to ensure that the password() method is called after username() method
    private val _usernameBeforePassword = MutableLiveData(false)
    val usernameBeforePassword: LiveData<Boolean> = _usernameBeforePassword

    // used to ensure that the navigation to registerKeyFragment is done after password() method
    private val _passwordBeforeNextTask = MutableLiveData(false)
    val passwordBeforeNextTask: LiveData<Boolean> = _passwordBeforeNextTask

    fun setPasswordBeforeNextTask() {
        _passwordBeforeNextTask.value = false
    }

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

    // used by summary fragment to send collected data
    fun sendUsername() {
        repository.username(username.value!!, _sendingUsername, _usernameBeforePassword)
    }
    fun sendPassword() {
        repository.password(password.value!!, _sendingPassword, _usernameBeforePassword, _passwordBeforeNextTask)
    }

    // used by register key fragment
    fun registerRequest() : LiveData<PendingIntent?> {
        return repository.registerRequest(_registeringKey)
    }
    fun registerResponse(intentData: Intent) {
        repository.registerResponse(intentData, _registeringKey)
    }

    //used by login fragment to sign in with key or fingerprint
    fun signInRequest(): LiveData<PendingIntent?> {
        return repository.signinRequest(_signInKey)
    }
    fun signInResponse(intentData: Intent) {
        repository.signinResponse(intentData, _signInKey)
    }

    //used in RegisterErrorFragment
    fun signOutOnError() {
        // clear all saved data of the current user
        repository.signOut()
        _username.value = ""
        _firstname.value = ""
        _lastname.value = ""
        _email.value = ""
    }

    //used by MainActivity toprevent multiple users being in signingIn state
    fun signOut() {
        repository.signOut()
    }
}