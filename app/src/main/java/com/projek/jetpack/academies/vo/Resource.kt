package com.projek.jetpack.academies.vo

class Resource<T>(val status:Status,val data:T?,val message:String?) {
    companion object{
        fun <T> succes(data:T?):Resource<T> = Resource(Status.SUCCES,data,null)
        fun<T> error(msg:String?,data:T?):Resource<T> =Resource(Status.ERROR,data,msg)
        fun<T> loading(data:T?):Resource<T> =Resource(Status.LOADING,data,null)

    }
}