package de.tuchemnitz.armadillogin

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import de.tuchemnitz.armadillogin.data.HelpData
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.ui.help.HelpItemAdapter

@BindingAdapter(value = ["textContent", "viewContext", "armadilloViewModel", "lifecycleOwner"])
fun bindText(recyclerView: RecyclerView, textContent: List<HelpData>, viewContext: FragmentActivity, armadilloViewModel: ArmadilloViewModel, lifecycleOwner: LifecycleOwner) {
    recyclerView.adapter = HelpItemAdapter(viewContext, textContent, armadilloViewModel, lifecycleOwner)
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

@BindingAdapter(value = ["subHeadlineDyslexicEnabled"])
fun adjustSubHeadlineFont(textView: TextView, subHeadlineDyslexicEnabled: Boolean) {
    if(subHeadlineDyslexicEnabled) {
        textView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Headline4)
    } else {
        textView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Headline4)
    }
}

@BindingAdapter(value = ["bigBodyDyslexicEnabled"])
fun adjustBigBodyFont(textView: TextView, bigBodyDyslexicEnabled: Boolean) {
    if(bigBodyDyslexicEnabled) {
        textView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Headline6)
    } else {
        textView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Headline6)
    }
}
