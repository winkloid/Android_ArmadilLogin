package de.tuchemnitz.armadillogin.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class HelpData(
        @StringRes val stringResourceId: Int,
        @DrawableRes val imageResourceId: Int? = null
)