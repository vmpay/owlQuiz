package eu.vmpay.owlquiz.pref

import android.os.Bundle
import android.preference.PreferenceFragment
import eu.vmpay.owlquiz.R

/**
 * Created by Andrew on 25/03/2018.
 */
class PrefFragment : PreferenceFragment(), PrefContract.View {

    override lateinit var presenter: PrefContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

//    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
//        if (view != null) {
//            Snackbar.make(view, "OnPrefChanged ${preference?.title} = $newValue", Snackbar.LENGTH_SHORT).show()
//        }
//        return true
//    }

    companion object {
        fun newInstance() = PrefFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        val preference = findPreference(getString(R.string.timer_sound_notification_key))
        preference.onPreferenceChangeListener = presenter
    }
}