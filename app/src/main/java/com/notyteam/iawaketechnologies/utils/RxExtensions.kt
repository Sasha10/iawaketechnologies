package com.notyteam.iawaketechnologies.utils

import android.view.View


fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

sealed class Result<T>
data class Success<T>(val value: T): Result<T>()
data class Failure<T>(val errorMessage: String, val errorCode: Int): Result<T>()
data class Loading<T>(val isLoading: Boolean): Result<T>()
