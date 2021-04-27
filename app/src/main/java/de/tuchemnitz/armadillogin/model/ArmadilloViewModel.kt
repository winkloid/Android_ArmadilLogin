package de.tuchemnitz.armadillogin.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class FragmentStatus { WELCOME, REGISTER_LOGIN, REGISTER1, REGISTER2, LOGIN }

class ArmadilloViewModel : ViewModel() {
    private val _status = MutableLiveData<FragmentStatus>()
    val status: LiveData<FragmentStatus> = _status


    fun setFragmentStatus(fragStatus: FragmentStatus) {
        _status.value = fragStatus
    }
}