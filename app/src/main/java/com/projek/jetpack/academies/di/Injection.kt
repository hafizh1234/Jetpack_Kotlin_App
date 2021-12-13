package com.projek.jetpack.academies.di

import android.content.Context
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.data.source.local.LocalDataSource
import com.projek.jetpack.academies.data.source.local.room.AcademyDatabase
import com.projek.jetpack.academies.data.source.remote.RemoteDataSource
import com.projek.jetpack.academies.utils.AppExecutors
import com.projek.jetpack.academies.utils.JsonHelper

object Injection {
    fun provideRepository(context: Context): AcademyRepository {
        val database = AcademyDatabase.getInstance(context)
        val localDataSource = LocalDataSource.getInstance(database.academyDao())
        val appExecutors = AppExecutors()
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        return AcademyRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}