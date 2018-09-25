package eu.vmpay.owlquiz.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


class InputManagerUtils {
    companion object {
        fun hideKeyboard(activity: Activity) {
            val view = activity.findViewById<View>(android.R.id.content)
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}