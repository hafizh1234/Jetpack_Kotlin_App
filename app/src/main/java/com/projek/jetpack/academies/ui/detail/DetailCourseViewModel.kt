package com.projek.jetpack.academies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.data.source.local.entity.CourseWithModules
import com.projek.jetpack.academies.vo.Resource

class DetailCourseViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    //private lateinit var courseId: String
    val courseId=MutableLiveData<String>()

    fun setSelectedCourse(courseId: String) {
        this.courseId.value = courseId
    }
    var courseModule:LiveData<Resource<CourseWithModules>> =Transformations.switchMap(courseId){ mcourseId->
        academyRepository.getCourseWithModules(mcourseId)
    }
    /*fun getCourse(): LiveData<CourseEntity> {
        return academyRepository.getCourseWithModules(courseId)
        /*
        lateinit var course:CourseEntity
        var courseEntities=DataDummy.generateDummyCourses()
        for(courseEntity in courseEntities){
            if(courseEntity.courseId==courseId){
                course=courseEntity
            }
        }
        return course
         */
    }

     */
    fun setBookmark(){
        val moduleResource=courseModule.value
        if(moduleResource!=null){
            val courseWithModule=moduleResource.data
            if(courseWithModule!=null){
                val courseEntity=courseWithModule.mCourseEntity
                val newState=!courseEntity.bookmarked
                academyRepository.setCourseBookmark(courseEntity,newState)
            }
        }
    }
    //fun getModules():LiveData<List<ModuleEntity>> =academyRepository.getAllModulesByCourse(courseId)

    //fun getModules():List<ModuleEntity> =DataDummy.generateDummyModules(courseId)
}