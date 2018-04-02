package eu.vmpay.owlquiz.activities.pref

import android.os.Bundle
import android.preference.PreferenceFragment
import eu.vmpay.owlquiz.AppController
import eu.vmpay.owlquiz.R

/**
 * Created by Andrew on 25/03/2018.
 */
class PrefFragment : PreferenceFragment(), PrefContract.View {

    lateinit var presenter: PrefContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        presenter = AppController.getInstance().prefPresenter
        presenter.takeView(this)

        val preference = findPreference(getString(R.string.timer_sound_notification_key))
        preference.onPreferenceChangeListener = AppController.getInstance().soundPlayer
    }
}