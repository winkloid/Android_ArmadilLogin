package de.tuchemnitz.armadillogin.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class FragmentStatus {
    WELCOME,
    REGISTER_LOGIN,
    REGISTER1,
    REGISTER2,
    REGISTER_SUMMARY,
    REGISTER_KEY,
    REGISTER_FINISHED,
    REGISTER_ERROR,
    LOGIN,
    LOGIN_KEY,
    USER_OVERVIEW,
    DEFAULT,
}

enum class FontStatus {
    HEADLINE,
    BODY,
    SWITCH,
}

class ArmadilloViewModel : ViewModel() {
    private val _status = MutableLiveData<FragmentStatus>()
    val status: LiveData<FragmentStatus> = _status

    private val _dyslexicFont = MutableLiveData<Boolean>(false)


    fun setFragmentStatus(fragStatus: FragmentStatus) {
        _status.value = fragStatus
        Log.d("SETSTATUS", "${status.value}")
    }

    fun setDyslexicFont(dyslexicActive: Boolean) {
        _dyslexicFont.value = dyslexicActive
    }

    /**
     * Determines whether a dyslexic font style or a standard font style is needed by checking [_dyslexicFont] value.
     * If a dyslexic font is needed, [getDyslexicFont] is called, else [getStandardFont] is called.
     */
    fun getCurrentFont(status: FontStatus) {
        if(_dyslexicFont.value!!) {
            getDyslexicFont(status)
        } else {
            getStandardFont(status)
        }
    }

    private fun getDyslexicFont(status: FontStatus) {}
    private fun getStandardFont(status: FontStatus) {}


}