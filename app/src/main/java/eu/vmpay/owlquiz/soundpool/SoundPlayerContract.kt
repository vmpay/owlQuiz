package eu.vmpay.owlquiz.soundpool

import android.preference.Preference

/**
 * Created by Andrew on 02/04/2018.
 */
interface SoundPlayerContract : Preference.OnPreferenceChangeListener {

    fun playSound(resId: Int)

    fun playSound()

    fun stopSound()
}