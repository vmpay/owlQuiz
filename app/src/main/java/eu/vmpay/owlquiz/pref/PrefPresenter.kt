package eu.vmpay.owlquiz.pref

import android.preference.Preference
import android.util.Log

/**
 * Created by Andrew on 30/03/2018.
 */
class PrefPresenter(val prefView: PrefContract.View) : PrefContract.Presenter {
    private val TAG = "PrefPresenter"

    init {
        prefView.presenter = this
    }

    override fun start() {
        Log.d(TAG, "start")
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        Log.d(TAG, "OnPrefChanged ${preference?.title} = $newValue")
        return true
    }
}