package com.projek.jetpack.academies.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.data.source.local.entity.CourseWithModules
import com.projek.jetpack.academies.data.source.local.room.AcademyDao

class LocalDataSource private constructor(private val mAcademyDao: AcademyDao) {
    companion object {
        private var INSTANCE: LocalDataSource? = null
        fun getInstance(academyDao: AcademyDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(academyDao)
    }

    fun getAllCourses(): DataSource.Factory<Int, CourseEntity> = mAcademyDao.getCourses()
    fun getBookmarkedCourses(): DataSource.Factory<Int, CourseEntity> = mAcademyDao.getBookmarked()
    fun getCourseWithModules(courseId: String): LiveData<CourseWithModules> =
        mAcademyDao.getCourseWithModuleById(courseId)

    fun getAllModulesByCourse(courseId: String): LiveData<List<ModuleEntity>> =
        mAcademyDao.getModulesByCourseId(courseId)

    fun insertModules(modules: List<ModuleEntity>) = mAcademyDao.insertModules(modules)
    fun insertCourses(courses: List<CourseEntity>) = mAcademyDao.insertCourses(courses)
    fun setCourseBookmark(course: CourseEntity, newState: Boolean) {
        course.bookmarked = newState
        mAcademyDao.updateCourse(course)
    }

    fun getModuleWithContent(moduleId: String): LiveData<ModuleEntity> =
        mAcademyDao.getModuleById(moduleId)

    fun updateContent(content: String, moduleId: String) {
        mAcademyDao.updateModuleByContent(content, moduleId)
    }

    fun setReadModule(module: ModuleEntity) {
        module.read = true
        mAcademyDao.updateModule(module)
    }
}