package com.projek.jetpack.academies.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.projek.jetpack.academies.data.source.local.LocalDataSource
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.local.entity.CourseWithModules
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.data.source.remote.ApiResponse
import com.projek.jetpack.academies.data.source.remote.RemoteDataSource
import com.projek.jetpack.academies.data.source.remote.response.ContentResponse
import com.projek.jetpack.academies.data.source.remote.response.CourseResponse
import com.projek.jetpack.academies.data.source.remote.response.ModuleResponse
import com.projek.jetpack.academies.utils.AppExecutors
import com.projek.jetpack.academies.vo.Resource

class AcademyRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    AcademyDataSource {
    companion object {
        @Volatile
        private var instance: AcademyRepository? = null
        fun getInstance(
            remoteData: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ): AcademyRepository =
            instance ?: synchronized(this) {
                instance ?: AcademyRepository(remoteData, localDataSource, appExecutors).apply {
                    instance = this
                }
            }

    }

    override fun getAllCourses(): LiveData<Resource<PagedList<CourseEntity>>> {
        return object :
            NetworkBoundResources<PagedList<CourseEntity>, List<CourseResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<CourseEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllCourses(),config).build()
            }

            override fun shouldFetch(data: PagedList<CourseEntity>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<CourseResponse>>> {
                return remoteDataSource.getAllCourses()
            }

            override fun saveCallResult(data: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in data) {
                    val course = CourseEntity(
                        response.id,
                        response.title,
                        response.description,
                        response.date,
                        false,
                        response.imagePath
                    )
                    courseList.add(course)
                }
                localDataSource.insertCourses(courseList)
            }
        }.asLiveData()
    }

    override fun getBookmarkedCourses(): LiveData<PagedList<CourseEntity>>{
        val config=PagedList.Config.Builder()
            .setPageSize(4)
            .setInitialLoadSizeHint(4)
            .setEnablePlaceholders(false)
            .build()
        return LivePagedListBuilder(localDataSource.getBookmarkedCourses(),config).build()
    }

    override fun getCourseWithModules(courseId: String): LiveData<Resource<CourseWithModules>> {
        return object :
            NetworkBoundResources<CourseWithModules, List<ModuleResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<CourseWithModules> =
                localDataSource.getCourseWithModules(courseId)

            override fun shouldFetch(data: CourseWithModules?): Boolean {
                return data?.mModules == null || data.mModules.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> =
                remoteDataSource.getAllModules(courseId)

            override fun saveCallResult(data: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (moduleResponse in data) {
                    val course = ModuleEntity(
                        moduleResponse.moduleId,
                        moduleResponse.courseId,
                        moduleResponse.title,
                        moduleResponse.position,
                        false
                    )
                    moduleList.add(course)
                }
                localDataSource.insertModules(moduleList)
            }
        }.asLiveData()
    }

    override fun getAllModulesByCourse(courseId: String): LiveData<Resource<List<ModuleEntity>>> {
        return object :
            NetworkBoundResources<List<ModuleEntity>, List<ModuleResponse>>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> =
                remoteDataSource.getAllModules(courseId)
            //pertanyaanya loadFromdb kenapa nggak make executor
            override fun loadFromDB(): LiveData<List<ModuleEntity>> =
                localDataSource.getAllModulesByCourse(courseId)

            override fun shouldFetch(data: List<ModuleEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun saveCallResult(data: List<ModuleResponse>) {

                //val moduleResult = MutableLiveData<List<ModuleEntity>>()

                val listModule = ArrayList<ModuleEntity>()
                for (response in data) {
                    val module = ModuleEntity(
                        response.moduleId,
                        response.courseId,
                        response.title,
                        response.position,
                        false
                    )
                    listModule.add(module)
                }
                localDataSource.insertModules(listModule)
            }
        }.asLiveData()
    }

    override fun getContent(moduleId: String): LiveData<Resource<ModuleEntity>> {
        return object : NetworkBoundResources<ModuleEntity, ContentResponse>(appExecutors) {
            override fun saveCallResult(data: ContentResponse) {
                //local data source disimpan sementara, karena cuman muat 1 list module content sama ini dipanggil di nbr
                //setelah dapat dapat data terbaru dari internet. Jadi, disimpan lagi di storage database.
                localDataSource.updateContent(data.content, moduleId)
            }

            override fun shouldFetch(data: ModuleEntity?): Boolean =
                data?.contentEntity == null

            override fun loadFromDB(): LiveData<ModuleEntity> =
                localDataSource.getModuleWithContent(moduleId)

            override fun createCall(): LiveData<ApiResponse<ContentResponse>> {
                return remoteDataSource.getContent(moduleId)
            }

        }.asLiveData()
    }

    override fun setCourseBookmark(course: CourseEntity, state: Boolean) {
        appExecutors.diskIO().execute{localDataSource.setCourseBookmark(course,state)}
    }

    override fun setReadModule(module: ModuleEntity) {
        appExecutors.diskIO().execute{localDataSource.setReadModule(module)}
    }


}