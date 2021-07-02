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

    // personal user data
    /**
     * Fistname of the user.
     *
     * Will not be saved after registration process. Just asked to make registration process more genuine.
     */
    private var _firstname = MutableLiveData("")
    val firstname: LiveData<String> = _firstname

    /**
     * Lastname of the user.
     *
     * Will not be saved after registration process. Just asked to make registration process more genuine.
     */
    private var _lastname = MutableLiveData("")
    val lastname: LiveData<String> = _lastname

    /**
     * email address of the user.
     *
     * Will not be saved after registration process. Just asked to make registration process more genuine.
     */
    private var _email = MutableLiveData("")
    val email: LiveData<String> = _email

    /**
     * Username created by the user.
     *
     * Will be saved on FIDO2 server after registration process to allow logging in with this username and a linked FIDO2 key.
     */
    private var _username = MutableLiveData("")
    val username: LiveData<String> = _username

    /**
     * Default passwort which will be sent to server with every registration request.
     *
     * With its server configuration the server accepts every password. To show the real advantages of fido2, no password is asked from the users
     */
    private val _password = MutableLiveData("defaultpass")
    private val password: LiveData<String> = _password

    // bools to show animations or adapt UI when user has to wait until a task has finished
    private val _sendingUsername = MutableLiveData<Boolean>()
    val sendingUsername: LiveData<Boolean> = _sendingUsername

    private val _sendingPassword = MutableLiveData<Boolean>()
    val sendingPassword: LiveData<Boolean> = _sendingPassword

    private val _registeringKey = MutableLiveData<Boolean>()
    val registeringKey: LiveData<Boolean> = _registeringKey

    private val _signInKey = MutableLiveData<Boolean>()
    val signInKey: LiveData<Boolean> = _signInKey

    /**
     * Used to ensure that the [sendPassword] method is called after [sendUsername] method
     */
    private val _usernameBeforePassword = MutableLiveData(false)
    val usernameBeforePassword: LiveData<Boolean> = _usernameBeforePassword

    /**
     * Indicates whether [sendPassword] method already finished.
     *
     * Used to ensure that the navigation to [RegisterKeyFragment][de.tuchemnitz.armadillogin.ui.register.RegisterKeyFragment] is done after [sendPassword] method completed when sending [username] and [password] to FIDO2 server in [RegisterSummaryFragment][de.tuchemnitz.armadillogin.ui.register.RegisterSummaryFragment].
     */
    private val _passwordBeforeNextTask = MutableLiveData(false)
    val passwordBeforeNextTask: LiveData<Boolean> = _passwordBeforeNextTask

    /**
     * Indicates whether signInResponse already completed and suceeded.
     *
     * Used to make sure that transition from [LoginKeyFragment][de.tuchemnitz.armadillogin.ui.login.LoginKeyFragment] to [UserOverviewFragment][de.tuchemnitz.armadillogin.ui.user.UserOverviewFragment]will be performed only after login is completed.
     */
    private val _signInReady = MutableLiveData(false)
    val signInReady: LiveData<Boolean> = _signInReady

    /**
     * Reset [_passwordBeforeNextTask] after sending all data.
     *
     * If [RegisterSummaryFragment][de.tuchemnitz.armadillogin.ui.register.RegisterSummaryFragment] is visited again, [_passwordBeforeNextTask] has to be reset to false.
     */
    fun setPasswordBeforeNextTask() {
        _passwordBeforeNextTask.value = false
    }

    /**
     * Reset [_usernameBeforePassword] after sending [username] and [password] to login.
     *
     * If [LoginFragment][de.tuchemnitz.armadillogin.ui.login.LoginFragment] is visited again, [_usernameBeforePassword] has to be reset to false.
     */
    fun setUsernameBeforePassword() {
        _usernameBeforePassword.value = false
    }

    /**
     * Reset [_signInKey] to false.
     */
    fun setSignInKey() {
        _signInKey.value = false
    }

    /**
     * Setup FIDO2 client
     *
     * Use [repository] to setup FIDO2 client.
     * Used by MainActivity to create FIDO2 client.
     */
    fun setFido2ApiClient(fidoClient: Fido2ApiClient?) {
        repository.setFido2APiClient(fidoClient)
    }

    /**
     * Store personal user data for registration process.
     *
     * Store data that were sent to ViewModel from [RegisterFragment][de.tuchemnitz.armadillogin.ui.register.RegisterFragment]
     */
    fun setData(fn: String, ln: String, email: String) {
        _firstname.value = fn
        _lastname.value = ln
        _email.value = email
    }

    /**
     * Store username created by user.
     *
     * Store the username the user entered in [RegisterUsernameFragment][de.tuchemnitz.armadillogin.ui.register.RegisterUserNameFragment]
     */
    fun setUsername(uname: String) {
        _username.value = uname
    }

    /**
     * Concatenate [_firstname] and [_lastname] to create full name.
     */
    fun getFullName(): String {
        return "${firstname.value} ${lastname.value}"
    }

    /**
     * Send to and register created username on FIDO2 server.
     */
    fun sendUsername() {
        repository.username(username.value!!, _sendingUsername, _usernameBeforePassword)
    }

    /**
     * Send default password to FIDO2 server.
     *
     * The server will accept every password. This is just a dummy value which is not asked from the user to show the real advantages of FIDO2 passwordless login.
     */
    fun sendPassword() {
        repository.password(
            password.value!!,
            _sendingPassword,
            _usernameBeforePassword,
            _passwordBeforeNextTask
        )
    }

    /**
     * Send a request to server to register a new FIDO2 key for current user.
     */
    fun registerRequest(): LiveData<PendingIntent?> {
        return repository.registerRequest(_registeringKey)
    }

    /**
     * Send result from the FIDO2 register intent to the server to complete the registration of the FIDO2 key.
     */
    fun registerResponse(intentData: Intent) {
        repository.registerResponse(intentData, _registeringKey)
    }

    /**
     * Send request to server to sign in into member area with current username.
     *
     * This will return an FIDO2 login intent.
     */
    fun signInRequest(): LiveData<PendingIntent?> {
        return repository.signinRequest(_signInKey)
    }

    /**
     * Send the response from the login intent to the server to complete the login process.
     */
    fun signInResponse(intentData: Intent) {
        repository.signinResponse(intentData, _signInKey, _signInReady)
    }

    /**
     * Once registration is finished and the user decides to go back to [RegisterLoginFragment][de.tuchemnitz.armadillogin.ui.registerlogin.RegisterLoginFragment], he/she will be signed out and all local data from the registration process will be deleted.
     */
    fun signOutOnErrorOrFinished() {
        // clear all saved data of the current user
        repository.signOut()
        _username.value = ""
        _firstname.value = ""
        _lastname.value = ""
        _email.value = ""
    }

    /**
     * Used by MainActivity to prevent multiple users being in signingIn state at once
     */
    fun signOut() {
        repository.signOut()
    }
}