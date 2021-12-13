package com.projek.jetpack.academies.ui.reader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.vo.Resource

class CourseReaderViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    var courseId=MutableLiveData<String>()
    var moduleId= MutableLiveData<String>()
    //fun getModules(): List<ModuleEntity> = DataDummy.generateDummyModules(courseId)
    //fun getModules():LiveData<List<ModuleEntity>> =academyRepository.getAllModulesByCourse(courseId)
    fun setSelectedModule(moduleId: String) {
        this.moduleId.value = moduleId
    }

    fun setSelectedCourse(courseId: String) {
        this.courseId.value = courseId
    }

    var selectedModule:LiveData<Resource<ModuleEntity>> =Transformations.switchMap(moduleId){
        selectedPosition->
        academyRepository.getContent(selectedPosition)
    }
    var modules:LiveData<Resource<List<ModuleEntity>>> =Transformations.switchMap(courseId){selectedCourseId->
        academyRepository.getAllModulesByCourse(selectedCourseId)
    }
        /*lateinit var module: ModuleEntity
        val moduleEntities = getModules()
        for (moduleEntity in moduleEntities) {
            if (moduleEntity.moduleId == moduleId) {
                module = moduleEntity
                module.contentEntity =
                    ContentEntity("<h3 class=\\\"fr-text-bordered\\\">" + module.title + "</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>")
                break
            }
        }

         */

        //return academyRepository.getContent(courseId,moduleId)
    fun readContent(module:ModuleEntity){
        academyRepository.setReadModule(module)
    }
    fun getModuleSize():Int{
        if(modules.value!=null){
            val moduleEntities=modules.value?.data
            if(moduleEntities!=null){
                return moduleEntities.size
            }
        }
        return 0
    }
    fun setNextPage(){
        if(selectedModule.value!=null&&modules.value!=null){
            val moduleEntity=selectedModule.value?.data
            val moduleEntities=modules.value?.data
            if(moduleEntities!=null&&moduleEntity!=null){
                val position=moduleEntity.position
                if(position<moduleEntities.size&&position>=0){
                    moduleId.value=moduleEntities[position+1].moduleId
                }
            }
        }
    }
    fun setPrevPage(){
        if(selectedModule.value!=null&&modules.value!=null){
            val moduleEntity=selectedModule.value?.data
            val moduleEntities= modules.value?.data
            if(moduleEntities!=null&&moduleEntity!=null){
                val position=moduleEntity.position
                if(position<moduleEntities.size&&position>=0){
                    moduleId.value=moduleEntities[position-1].moduleId
                }
            }
        }
    }
}