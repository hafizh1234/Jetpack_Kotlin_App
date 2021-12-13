package com.projek.jetpack.academies.utils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource


object IdlingResource {
    private val RESOURCE="GLOBAL"
    fun getIdlingResource():IdlingResource{
        return idlingResource
    }
    val idlingResource= CountingIdlingResource(RESOURCE)
    fun increment(){
        idlingResource.increment()
    }
    fun decrement(){
        idlingResource.decrement()
    }


}