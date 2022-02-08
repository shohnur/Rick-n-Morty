package uz.task.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import uz.task.network.models.AllCharactersResp

interface ApiInterface {

    @GET("character")
    fun getAllCharacters(
        @Query("page") page:Int
    ): Single<AllCharactersResp>

}