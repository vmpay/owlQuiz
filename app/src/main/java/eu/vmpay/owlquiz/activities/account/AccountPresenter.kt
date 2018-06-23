package eu.vmpay.owlquiz.activities.account

import android.util.Log
import eu.vmpay.owlquiz.repository.PlayersRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Andrew on 12/04/2018.
 */
class AccountPresenter(val playersRepository: PlayersRepository) : AccountContract.Presenter {

    private lateinit var accountView: AccountContract.View

    override fun takeView(view: AccountContract.View) {
        accountView = view
        playersRepository.searchForPlayer("", "Андрей", "Юрьевич")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d("retrofit", "Searching for players size ${result.size}")
                    result.filter { it.surname == "Ярковой" }.forEach { Log.d("retrofit", "player $it") }
                }, { error ->
                    error.printStackTrace()
                })
    }

    override fun dropView() {
    }
}