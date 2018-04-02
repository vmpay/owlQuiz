package eu.vmpay.owlquiz.activities.pref

import android.preference.Preference
import eu.vmpay.owlquiz.BasePresenter
import eu.vmpay.owlquiz.BaseView

/**
 * Created by Andrew on 30/03/2018.
 */
interface PrefContract {

    interface View : BaseView<Presenter> {

        var isActive: Boolean

    }

    interface Presenter : BasePresenter<View>, Preference.OnPreferenceChangeListener
}