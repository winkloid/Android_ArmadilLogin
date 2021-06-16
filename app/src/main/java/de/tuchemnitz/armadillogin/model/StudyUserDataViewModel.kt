package de.tuchemnitz.armadillogin.model

import androidx.lifecycle.ViewModel

class StudyUserDataViewModel: ViewModel() {
    // time measurement feature
    var userStartTime: Long? = null
    var userFinishedTime: Long? = null
    var userTime: Long? = null

    fun calculateUserTime() {
        if (userFinishedTime != null && userStartTime != null) {
            userTime = userFinishedTime!! - userStartTime!!
        }
    }
}