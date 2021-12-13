package com.projek.jetpack.academies.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.local.entity.CourseWithModules
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.vo.Resource

interface AcademyDataSource {
    fun getAllCourses(): LiveData<Resource<PagedList<CourseEntity>>>
    fun getBookmarkedCourses(): LiveData<PagedList<CourseEntity>>
    fun getCourseWithModules(courseId:String): LiveData<Resource<CourseWithModules>>
    fun getAllModulesByCourse(courseId: String): LiveData<Resource<List<ModuleEntity>>>
    fun getContent(moduleId: String): LiveData<Resource<ModuleEntity>>
    fun setCourseBookmark(course:CourseEntity,state:Boolean)
    fun setReadModule(module:ModuleEntity)
}