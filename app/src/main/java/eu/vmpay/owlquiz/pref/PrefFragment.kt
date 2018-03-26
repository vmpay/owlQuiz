package eu.vmpay.owlquiz.pref

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import eu.vmpay.owlquiz.R

/**
 * Created by Andrew on 25/03/2018.
 */
class PrefFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)


    }

    fun setOnPreferenceChangeListenerAndFire(key: String, listener: (Preference, Any) -> Boolean) {
        val preference = findPreference(key)
        preference.setOnPreferenceChangeListener(listener)
        listener(preference, preferenceManager.sharedPreferences.getString(key, ""))
    }
}