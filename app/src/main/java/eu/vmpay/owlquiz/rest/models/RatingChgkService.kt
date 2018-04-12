package eu.vmpay.owlquiz.rest.models

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Andrew on 04/04/2018.
 */
interface RatingChgkService {

    /**
     * Companion object to create the RatingChgkService
     */
    companion object Factory {
        fun create(): RatingChgkService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://rating.chgk.info/")
                    .build()

            return retrofit.create(RatingChgkService::class.java)
        }
    }

    @GET("api/players/{userId}")
    fun searchPlayer(@Path("userId") query: Long): Observable<List<Player>>

}