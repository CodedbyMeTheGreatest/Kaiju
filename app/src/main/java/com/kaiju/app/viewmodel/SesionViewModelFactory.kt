package com.kaiju.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.repository.AlmacenSesion

class SesionViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val almacen = AlmacenSesion(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SesionViewModel(almacen) as T
}