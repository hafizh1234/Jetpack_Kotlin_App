package com.projek.jetpack.academies.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.di.Injection
import com.projek.jetpack.academies.ui.academy.AcademyViewModel
import com.projek.jetpack.academies.ui.bookmark.BookMarkViewModel
import com.projek.jetpack.academies.ui.detail.DetailCourseViewModel
import com.projek.jetpack.academies.ui.reader.CourseReaderViewModel

class ViewModelFactory private constructor(private val mAcademyRepository: AcademyRepository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                    instance = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AcademyViewModel::class.java) -> {
                return AcademyViewModel(mAcademyRepository) as T
            }
            modelClass.isAssignableFrom(DetailCourseViewModel::class.java) -> {
                return DetailCourseViewModel(mAcademyRepository) as T
            }
            modelClass.isAssignableFrom(CourseReaderViewModel::class.java) -> {
                return CourseReaderViewModel(mAcademyRepository) as T
            }
            modelClass.isAssignableFrom(BookMarkViewModel::class.java) -> {
                return BookMarkViewModel(mAcademyRepository) as T
            }
            else -> {
                throw Throwable("Unknown ViewModel Class: ${modelClass.name}")
            }
        }
    }
}