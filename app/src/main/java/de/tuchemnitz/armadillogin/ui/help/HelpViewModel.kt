package de.tuchemnitz.armadillogin.ui.help

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import androidx.fragment.app.activityViewModels
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.model.HelpData

class HelpViewModel : ViewModel() {

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
        return listOf<HelpData>(
                HelpData(R.string.help1),
                HelpData(R.string.help2),
                HelpData(R.string.help3),
                HelpData(R.string.help4),
                HelpData(R.string.help5),
                HelpData(R.string.help6),
                HelpData(R.string.help7),
                HelpData(R.string.help8),
                HelpData(R.string.help9),
                HelpData(R.string.help10)
        )
    }
}