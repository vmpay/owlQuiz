package eu.vmpay.owlquiz.utils

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar


class SnackbarMessage : SingleLiveEvent<Int>() {

    fun observe(owner: LifecycleOwner, observer: SnackbarObserver<in Int>) {
        super.observe(owner, Observer { if (it != null) observer.onNewMessage(it) })
    }

    interface SnackbarObserver<Int> {
        /**
         * Called when there is a new message to be shown.
         * @param snackbarMessageResourceId The new message, non-null.
         */
        fun onNewMessage(@StringRes snackbarMessageResourceId: Int)
    }

    companion object {
        fun showSnackbar(view: View?, snackbarStringText: String?) {
            if (view != null && snackbarStringText != null)
                Snackbar.make(view, snackbarStringText, Snackbar.LENGTH_LONG).show()
        }
    }
}
