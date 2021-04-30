package de.tuchemnitz.armadillogin.data

import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.model.HelpData

class HelpDataSource {
    fun loadHelpData(): List<HelpData> {
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