package com.notyteam.iawaketechnologies.api.responses.programs


import com.google.gson.annotations.SerializedName
import com.notyteam.iawaketechnologies.api.responses.BaseResponse
import noty_team.com.masterovik.ui.adapters.items_adapter.ProgramsItemLocal
import javax.annotation.Generated


@Generated("com.robohorse.robopojogenerator")
data class ListProgramsResponse(
    @field:SerializedName("errors")
    val error: String? = null,

    @field:SerializedName("programs")
    val programs: ArrayList<ProgramsItem>? = null
) : BaseResponse() {

    fun map(): ArrayList<ProgramsItemLocal> {
        return programs?.flatMap {

            arrayListOf(
                ProgramsItemLocal(
                    id = it.id ?: "",
                    title = it.title ?: "",
                    descriptionHTML = it.descriptionHTML ?: "",
                    cover = it.cover,
                    tracks = it.tracks
                )
            )


        } as ArrayList
    }


}

