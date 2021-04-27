package de.tuchemnitz.armadillogin

import android.widget.TextView
import de.tuchemnitz.armadillogin.model.FragmentStatus
import androidx.databinding.BindingAdapter

@BindingAdapter("textInsert")
fun bindText(textView: TextView, status: FragmentStatus?) {
    textView.setText("Status: ${status}")
}