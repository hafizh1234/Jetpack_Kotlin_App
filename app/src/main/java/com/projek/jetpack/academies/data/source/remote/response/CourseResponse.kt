package com.projek.jetpack.academies.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseResponse(
    var id: String,
    var title: String,
    var description: String,
    var imagePath: String,
    var date: String
):Parcelable