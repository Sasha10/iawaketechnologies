package com.notyteam.iawaketechnologies.api.retfofit.apis

import android.content.Context
import com.notyteam.iawaketechnologies.App
import javax.inject.Inject

class BaseApi @Inject constructor(val context: Context) {

    @Inject
    lateinit var programsApi: ProgramsApi

    init {
        App[context].appComponent.inject(this)
    }

}
