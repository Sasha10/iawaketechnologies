package com.notyteam.iawaketechnologies.api.retfofit.api_interface


import com.notyteam.iawaketechnologies.api.responses.programs.ListProgramsResponse
import io.reactivex.Single
import retrofit2.http.GET


interface ProgramsApiInterface {

    @GET("media-library/free")
    fun getPrograms(): Single<ListProgramsResponse>

}