package de.tuchemnitz.armadillogin

import android.widget.TextView
import de.tuchemnitz.armadillogin.model.FragmentStatus
import androidx.databinding.BindingAdapter
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel

@BindingAdapter("textInsert")
fun bindText(textView: TextView, textInsert: String) {
    textView.setText("Status: ${textInsert}")
}