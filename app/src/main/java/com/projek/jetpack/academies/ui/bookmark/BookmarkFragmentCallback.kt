package com.projek.jetpack.academies.ui.bookmark

import com.projek.jetpack.academies.data.source.local.entity.CourseEntity

interface BookmarkFragmentCallback {
    fun onShareClick(course: CourseEntity)
}
