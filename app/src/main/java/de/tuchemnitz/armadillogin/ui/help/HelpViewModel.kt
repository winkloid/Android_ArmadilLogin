package de.tuchemnitz.armadillogin.ui.help

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import androidx.fragment.app.activityViewModels
import de.tuchemnitz.armadillogin.model.FragmentStatus

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
}