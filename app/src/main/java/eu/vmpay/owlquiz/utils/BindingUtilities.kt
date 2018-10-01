package eu.vmpay.owlquiz.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

@BindingAdapter("app:progressTime")
fun setProgressTime(textView: TextView, millis: Int) {
    textView.text = java.lang.String.format(Locale.US, "%.1f", millis.toDouble() / 1000)
}