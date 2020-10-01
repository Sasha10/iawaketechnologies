package com.notyteam.iawaketechnologies.api.responses.programs

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class ProgramsItem(
    @field:SerializedName("descriptionHTML")
    val descriptionHTML: String?,
    @field:SerializedName("id")
    val id: String?,
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("cover")
    val cover: Cover? = null,
    @field:SerializedName("tracks")
    val tracks: ArrayList<TracksItem?>? = null
)

data class Cover(
    @field:SerializedName("url")
    val url: String?
)
