package eu.vmpay.owlquiz.activities.pref

import android.os.Bundle
import android.preference.PreferenceFragment
import eu.vmpay.owlquiz.R
import eu.vmpay.owlquiz.utils.InjectorUtils

/**
 * Created by Andrew on 25/03/2018.
 */
class PrefFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        val preference = findPreference(getString(R.string.timer_sound_notification_key))
        preference.onPreferenceChangeListener = InjectorUtils.getSoundPlayer(activity)
    }
}