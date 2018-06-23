package eu.vmpay.owlquiz.rest.models

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
    fun searchPlayerById(@Path("userId") query: Long): Observable<List<Player>>

    @GET("api/players.json/search")
    fun searchPlayerByFullName(@Query("surname") surname: String,
                               @Query("name") name: String,
                               @Query("patronymic") patronymic: String)
            : Observable<PlayerSearch>

}