package com.romoreno.compraplus.ui.finder

sealed class FinderState {

    data class Success(val products:String):FinderState()
    data class Error(val error:String):FinderState()
    data object Loading:FinderState()

}