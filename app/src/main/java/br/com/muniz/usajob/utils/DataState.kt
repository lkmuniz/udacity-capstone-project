package br.com.muniz.usajob.utils

sealed class DataState {
    object Loading : DataState()
    object Error : DataState()
    object Success : DataState()
}