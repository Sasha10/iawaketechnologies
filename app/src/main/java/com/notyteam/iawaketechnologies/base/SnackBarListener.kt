package com.notyteam.iawaketechnologies.base

interface SnackBarListener {
    fun showErrorSnack(message: String)
    fun showSuccessSnack(message: String)

    companion object {
        val empty = object : SnackBarListener {
            override fun showErrorSnack(message: String) {}
            override fun showSuccessSnack(message: String) {}
        }
    }
}