package de.tuchemnitz.armadillogin.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


data class HelpData(
        @StringRes val titleResourceId: Int,
        @StringRes val stringResourceId: Int,
        @DrawableRes val imageResourceId: Int? = null
)