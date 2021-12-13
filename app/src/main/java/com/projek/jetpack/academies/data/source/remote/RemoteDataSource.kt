package com.projek.jetpack.academies.data.source.remote

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projek.jetpack.academies.data.source.remote.response.ContentResponse
import com.projek.jetpack.academies.data.source.remote.response.CourseResponse
import com.projek.jetpack.academies.data.source.remote.response.ModuleResponse
import com.projek.jetpack.academies.utils.IdlingResource
import com.projek.jetpack.academies.utils.JsonHelper

class RemoteDataSource private constructor(private val jsonHelper: JsonHelper) {
    private val handler = Handler(Looper.getMainLooper())

    companion object {

        private const val SERVICE_LATENCY_IN_MILIS = 2000L

        @Volatile
        private var instance: RemoteDataSource? = null
        fun getInstance(helper: JsonHelper): RemoteDataSource =
            instance ?: synchronized(this) {
                RemoteDataSource(helper).apply {
                    instance = this
                }
            }
    }

    fun getAllCourses(): LiveData<ApiResponse<List<CourseResponse>>> {
        IdlingResource.increment()
        val resultCourse = MutableLiveData<ApiResponse<List<CourseResponse>>>()
        handler.postDelayed(
            {
                resultCourse.value = ApiResponse.succes(jsonHelper.loadCourse())
                IdlingResource.decrement()
            },
            SERVICE_LATENCY_IN_MILIS
        )
        return resultCourse
    }

    interface LoadCourseCallback {
        fun onAllCoursesReceived(courses: List<CourseResponse>)
    }

    fun getAllModules(courseId: String): LiveData<ApiResponse<List<ModuleResponse>>> {
        IdlingResource.increment()
        val resultModules = MutableLiveData<ApiResponse<List<ModuleResponse>>>()
        handler.postDelayed(
            {
                resultModules.value = ApiResponse.succes(jsonHelper.loadModule(courseId))
                IdlingResource.decrement()
            },
            SERVICE_LATENCY_IN_MILIS
        )
        return resultModules
    }

    interface LoadModulesCallback {
        fun onAllModulesReceived(moduleResponses: List<ModuleResponse>)
    }

    fun getContent(moduleId: String): LiveData<ApiResponse<ContentResponse>> {
        IdlingResource.increment()
        val resultContent = MutableLiveData<ApiResponse<ContentResponse>>()
        handler.postDelayed(
            {
                resultContent.value = ApiResponse.succes(jsonHelper.loadContent(moduleId))
                IdlingResource.decrement()
            }, SERVICE_LATENCY_IN_MILIS
        )
        return resultContent
    }

    interface LoadContentCallback {
        fun onContentReceived(contentResponse: ContentResponse)
    }

}
