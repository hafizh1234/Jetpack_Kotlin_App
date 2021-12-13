package com.projek.jetpack.academies.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity

class BookMarkViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    //fun getAllBookmark(): List<CourseEntity> = DataDummy.generateDummyCourses()
    fun getAllBookmark():LiveData<PagedList<CourseEntity>> =academyRepository.getBookmarkedCourses()
    fun setBookmark(courseEntity: CourseEntity){
        val state=!courseEntity.bookmarked
        academyRepository.setCourseBookmark(courseEntity,state)
    }
}