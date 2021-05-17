package de.tuchemnitz.armadillogin.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.tuchemnitz.armadillogin.R

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

class ArmadilloViewModel : ViewModel() {
    private val _status = MutableLiveData<FragmentStatus>()
    val status: LiveData<FragmentStatus> = _status

    private val _dyslexicFont = MutableLiveData<Boolean>(false)

    private val _currentHeadlineStyle = MutableLiveData<Int>(R.style.TextAppearance_StandardTypographyStyles_Headline3)
    val currentHeadlineStyle: LiveData<Int> = _currentHeadlineStyle


    fun setFragmentStatus(fragStatus: FragmentStatus) {
        _status.value = fragStatus
        Log.d("SETSTATUS", "${status.value}")
    }

    fun setDyslexicFont(dyslexicActive: Boolean) {
        _dyslexicFont.value = dyslexicActive
        getHeadlineFont()
    }

    /**
     * Determines whether a dyslexic font style or a standard font style is needed by checking [_dyslexicFont] value.
     * If a dyslexic font is needed, [getDyslexicFont] is called, else [getStandardFont] is called.
     */
    fun getHeadlineFont() {
        if(_dyslexicFont.value!!) {
            _currentHeadlineStyle.value = R.style.TextAppearance_DyslexicTypographyStyles_Headline3
        } else {
            _currentHeadlineStyle.value = R.style.TextAppearance_StandardTypographyStyles_Headline3
        }
    }


}