package com.notyteam.iawaketechnologies.api.retfofit.apis

import android.content.Context
import com.notyteam.iawaketechnologies.api.responses.programs.ListProgramsResponse
import com.notyteam.iawaketechnologies.api.retfofit.BaseRepository
import com.notyteam.iawaketechnologies.api.retfofit.api_interface.ProgramsApiInterface
import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject
import com.notyteam.iawaketechnologies.utils.Result

class ProgramsApi @Inject constructor(
        context: Context,
        retrofit: Retrofit,
        private var programsApi: ProgramsApiInterface
): BaseRepository(context, retrofit) {

    fun getPrograms(onResult: (Result<ListProgramsResponse>) -> Unit): Single<ListProgramsResponse> {
            return initRequestSingleForResult(programsApi.getPrograms(), onResult)
    }

}
