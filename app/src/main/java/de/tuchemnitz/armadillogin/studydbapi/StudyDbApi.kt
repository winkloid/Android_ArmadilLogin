package de.tuchemnitz.armadillogin.studydbapi

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.fido2api.AuthenticationApi
import de.tuchemnitz.armadillogin.fido2user.AuthRepository
import okhttp3.Call
import okhttp3.Response
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class StudyDbApi(private val executor: Executor) {
   /* companion object {
        private var instance: StudyDbApi? = null

        // Constructor
        fun getInstance(): StudyDbApi {
            return instance ?: synchronized(this) {
                instance ?: StudyDbApi(
                    Executors.newFixedThreadPool(64)
                ).also { instance = it }
            }
        }
    }
    val respone: Response? = null

    fun createUser(call: Call): Response {
        executor.execute {
            call.execute()
        }
    }*/
}