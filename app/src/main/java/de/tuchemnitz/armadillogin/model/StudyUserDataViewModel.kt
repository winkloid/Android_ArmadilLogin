package de.tuchemnitz.armadillogin.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudyUserDataViewModel: ViewModel() {
    // time measurement feature
    var userStartTime: Long = 0
    var userFinishedTime: Long = 0
    var userTime: Long = 0

    fun calculateUserTime() {
        userTime = userFinishedTime - userStartTime
    }

    // personal user data for study and statistics
    /**
     * Represents the age of the user.
     * null - not specified
     * else - the actual age
     */
    private var _age = MutableLiveData<Int?>(null)
    val age: LiveData<Int?> = _age

    fun setAge(ageInput: Int?) {
        _age.value = ageInput
    }

    /**
     * Represents the gender of the user.
     * 0 - not specified
     * 1 - Female
     * 2 - Male
     * 3 - Diverse
     */
    private var _gender = MutableLiveData<Int>(0)
    val gender: LiveData<Int> = _gender

    /**
     * Sets the gender specified by the user immediately when user selects it from radio group in UserDataFragment.
     */
    fun setGender(genderInput: Int) {
        _gender.value = genderInput
    }

    /**
     * Represents technical experience of the user.
     * 0 - not specified
     * 1 - none
     * 2 - little
     * 3 - average
     * 4 - much
     * 5 - expert level
     */
    private var _technicalExperience = MutableLiveData<Int>(0)
    val technicalExperience: LiveData<Int> = _technicalExperience

    /**
     * Sets the gender specified by the user immediately when user selects it from radio group in UserDataFragment.
     */
    fun setTechnicalExperience(technicalExperienceInput: Int) {
        _technicalExperience.value = technicalExperienceInput
    }
}