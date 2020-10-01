package com.notyteam.iawaketechnologies.api.responses.programs

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class TracksItem (
    @field:SerializedName("key")
    val key: String?,
    @field:SerializedName("duration")
    val duration: Int?,
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("media")
    val media: Media?
) {
    constructor() : this("",0,"",null)
}

data class Media(
    @field:SerializedName("mp3")
    val mp3: Url?
)
data class Url(
    @field:SerializedName("url")
    val url: String?
)