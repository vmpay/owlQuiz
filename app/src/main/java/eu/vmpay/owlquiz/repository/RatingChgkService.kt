package eu.vmpay.owlquiz.repository

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://rating.chgk.info/")
                    .client(httpClient.build())
                    .build()

            return retrofit.create(RatingChgkService::class.java)
        }
    }

    @GET("api/players/{playerId}")
    fun searchPlayerById(@Path("playerId") query: Long): Observable<List<Player>>

    @GET("api/players.json/search")
    fun searchPlayerByFullName(@Query("surname") surname: String,
                               @Query("name") name: String,
                               @Query("patronymic") patronymic: String)
            : Observable<PlayerSearch>

    @GET("api/players/{playerId}/rating")
    fun getPlayerRating(@Path("playerId") playerId: Long): Observable<List<PlayerRating>>

    @GET("api/players/{playerId}/teams")
    fun getPlayerTeam(@Path("playerId") playerId: Long): Observable<List<PlayerTeam>>

    @GET("api/teams/{teamId}")
    fun getTeamById(@Path("teamId") teamId: Long): Observable<List<Team>>

    @GET("api/teams/{teamId}/rating")
    fun getTeamRatings(@Path("teamId") teamId: Long): Observable<List<TeamRating>>

}