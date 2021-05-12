package de.tuchemnitz.armadillogin.ui.help

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.data.HelpData
import java.io.InputStream
import java.nio.channels.AsynchronousFileChannel.open

class HelpViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val LOG_TAG = "HELP_VIEWMODEL"
    }

    var parseInputStream: InputStream = application.assets.open("helpdata.xml")
    private val parsedHelpList = HelpDataXmlParser().parse(parseInputStream)

    fun getCurrentHelp(status: FragmentStatus?): String {
        return when(status) {
            FragmentStatus.WELCOME -> "Willkommen"
            FragmentStatus.LOGIN -> "Einloggen"
            FragmentStatus.REGISTER_LOGIN -> "Registrieren oder einloggen"
            FragmentStatus.REGISTER1 -> "Persönliche Daten eingeben"
            FragmentStatus.REGISTER2 -> "Nutzernamen vergeben"
            FragmentStatus.REGISTER_SUMMARY -> "Bestätige die Richtigkeit der eingegebenen Daten oder passe Sie an."
            FragmentStatus.REGISTER_KEY -> "Registriere deinen Schlüssel."
            else -> "Strange thing..."
        }
    }

    fun loadHelpData(status: FragmentStatus?): List<HelpData> {
        Log.d(LOG_TAG, "parsedList: $parsedHelpList")
        return listOf<HelpData>(
                HelpData(R.string.help1, R.drawable.image1),
                HelpData(R.string.help2),
                HelpData(R.string.help3),
                HelpData(R.string.help4),
                HelpData(R.string.help5, R.drawable.image2),
                HelpData(R.string.help6),
                HelpData(R.string.help7, R.drawable.image3),
                HelpData(R.string.help8),
                HelpData(R.string.help9),
                HelpData(R.string.help10)
        )
    }
}