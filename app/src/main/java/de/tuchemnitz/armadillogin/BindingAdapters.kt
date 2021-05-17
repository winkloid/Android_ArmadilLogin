package de.tuchemnitz.armadillogin

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import de.tuchemnitz.armadillogin.data.HelpData
import de.tuchemnitz.armadillogin.ui.help.HelpItemAdapter

@BindingAdapter(value = ["textContent", "viewContext"])
fun bindText(recyclerView: RecyclerView, textContent: List<HelpData>, viewContext: FragmentActivity) {
    recyclerView.adapter = HelpItemAdapter(viewContext, textContent)
    recyclerView.setHasFixedSize(true)
}

@BindingAdapter(value = ["headlineDyslexicEnabled"])
fun adjustHeadlineFont(textView: TextView, headlineDyslexicEnabled: Boolean) {
    if(headlineDyslexicEnabled) {
        textView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Headline3)
    } else {
        textView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Headline3)
    }
}

@BindingAdapter(value = ["bodyDyslexicEnabled"])
fun adjustBodyFont(textView: TextView, bodyDyslexicEnabled: Boolean) {
    if(bodyDyslexicEnabled) {
        textView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Body1)
    } else {
        textView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Body1)
    }
}