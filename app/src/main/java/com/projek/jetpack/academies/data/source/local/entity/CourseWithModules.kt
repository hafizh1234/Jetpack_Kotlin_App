package com.projek.jetpack.academies.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithModules(
    @Embedded
    var mCourseEntity: CourseEntity,
    @Relation(parentColumn = "courseId", entityColumn = "courseId")
    var mModules: List<ModuleEntity>
)