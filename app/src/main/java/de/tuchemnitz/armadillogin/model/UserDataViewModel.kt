package de.tuchemnitz.armadillogin.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.tuchemnitz.armadillogin.databinding.FragmentRegisterBinding

class UserDataViewModel : ViewModel() {
    private var _firstname = MutableLiveData<String>("Test")
    val firstname: LiveData<String> = _firstname

    private var _lastname = MutableLiveData<String>("User")
    val lastname: LiveData<String> = _lastname

    private var _email = MutableLiveData("klaus.uhr@tu-chemnitz.de")
    val email: LiveData<String> = _email

    private var _username = MutableLiveData("username")
    val username: LiveData<String> = _username

    fun setFirstname(fn: String): Boolean {
        _firstname.value = fn
        return true
    }

}