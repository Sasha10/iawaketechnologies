package com.notyteam.iawaketechnologies.api.responses

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
open class BaseResponse(
    @field:SerializedName("error_message")
    val errorMessage: String = "",

    @field:SerializedName("status_code")
    val statusCode: String = ""
)