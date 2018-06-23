package eu.vmpay.owlquiz.activities.account

import eu.vmpay.owlquiz.BasePresenter
import eu.vmpay.owlquiz.BaseView

/**
 * Created by Andrew on 12/04/2018.
 */
interface AccountContract {

    interface View : BaseView<Presenter> {

        var isActive: Boolean

    }

    interface Presenter : BasePresenter<View> {

    }
}