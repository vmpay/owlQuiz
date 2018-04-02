package eu.vmpay.owlquiz.activities.pref

import android.preference.Preference
import android.util.Log

/**
 * Created by Andrew on 30/03/2018.
 */
class PrefPresenter : PrefContract.Presenter {

    private lateinit var prefView: PrefContract.View

    private val TAG = "PrefPresenter"

    override fun takeView(view: PrefContract.View) {
        prefView = view
    }

    override fun dropView() {
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        Log.d(TAG, "OnPrefChanged ${preference?.title} = $newValue")
        return true
    }
}