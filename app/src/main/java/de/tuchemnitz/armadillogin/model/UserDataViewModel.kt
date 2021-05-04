package de.tuchemnitz.armadillogin.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _sendingUsername = MutableLiveData<Boolean>()
    val sendingUsername: LiveData<Boolean> = _sendingUsername

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

}