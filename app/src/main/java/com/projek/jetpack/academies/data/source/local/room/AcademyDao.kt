package com.projek.jetpack.academies.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.data.source.local.entity.CourseWithModules

@Dao
interface AcademyDao {
    @Query("SELECT * FROM courseentities")
    fun getCourses(): DataSource.Factory<Int,CourseEntity>

    @Query("SELECT * FROM courseentities WHERE bookmarked=1")
    fun getBookmarked(): DataSource.Factory<Int,CourseEntity>

    @Transaction
    @Query("SELECT * FROM courseentities WHERE courseId= :courseId")
    fun getCourseWithModuleById(courseId: String): LiveData<CourseWithModules>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(courses: List<CourseEntity>)

    @Update
    fun updateCourse(course: CourseEntity)

    @Query("SELECT * FROM moduleentities WHERE courseId =:courseId")
    fun getModulesByCourseId(courseId: String): LiveData<List<ModuleEntity>>

    @Query("SELECT * FROM moduleentities WHERE moduleId =:moduleId")
    fun getModuleById(moduleId: String): LiveData<ModuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModules(module: List<ModuleEntity>)

    @Update
    fun updateModule(module: ModuleEntity)

    @Query("UPDATE moduleentities SET content =:content WHERE moduleId=:moduleId")
    fun updateModuleByContent(content: String, moduleId: String)
}