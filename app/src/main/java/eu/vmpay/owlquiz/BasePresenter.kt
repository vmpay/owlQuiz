package eu.vmpay.owlquiz

/**
 * Created by Andrew on 24/03/2018.
 */
interface BasePresenter<T> {

    fun takeView(view: T)

    fun dropView()

}