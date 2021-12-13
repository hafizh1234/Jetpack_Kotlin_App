package com.projek.jetpack.academies.ui.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.vo.Resource

class AcademyViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    //fun getCourses(): List<CourseEntity> = DataDummy.generateDummyCourses()
    fun getCourses(): LiveData<Resource<PagedList<CourseEntity>>> =academyRepository.getAllCourses()
}