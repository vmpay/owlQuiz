package eu.vmpay.owlquiz.activities.account

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vmpay.owlquiz.AppController
import eu.vmpay.owlquiz.R

/**
 * A placeholder fragment containing a simple view.
 */
class AccountActivityFragment : Fragment(), AccountContract.View {

    override var isActive: Boolean = false
        get() = isAdded

    private lateinit var presenter: AccountPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        presenter = AppController.getInstance().accountPresenter
        presenter.takeView(this)
        return view
    }
}
