package com.projek.jetpack.academies.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nhaarman.mockitokotlin2.verify
import com.projek.jetpack.academies.PagedListUtils
import com.projek.jetpack.academies.data.source.local.LocalDataSource
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.local.entity.CourseWithModules
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.data.source.remote.RemoteDataSource
import com.projek.jetpack.academies.utils.AppExecutors
import com.projek.jetpack.academies.utils.DataDummy
import com.projek.jetpack.academies.utils.TestLive
import com.projek.jetpack.academies.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class AcademyRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private val local= Mockito.mock(LocalDataSource::class.java)
    private val appExecutors=Mockito.mock(AppExecutors::class.java)
    private val academyRepository = FakeAcademyRepository(remote,local,appExecutors)
    private val courseResponse = DataDummy.generateRemoteDummyCourses()
    private val courseId = courseResponse[0].id
    private val moduleResponses = DataDummy.generateRemoteDummyModules(courseId)
    private val moduleId = moduleResponses[0].moduleId
    private val content = DataDummy.generateRemoteDummyContent(moduleId)

    @Test
    fun getAllCourses() {
        /*
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadCourseCallback)
                .onAllCoursesReceived(courseResponse)
            null
        }.`when`(remote).getAllCourses(any())

         */
        val dataSourceFactory= mock(DataSource.Factory::class.java) as DataSource.Factory<Int, CourseEntity>
        //val dummyCourses=MutableLiveData<List<CourseEntity>>()
        //dummyCourses.value=DataDummy.generateDummyCourses()
        `when`(local.getAllCourses()).thenReturn(dataSourceFactory)
        academyRepository.getAllCourses()
        val courseEntities = Resource.succes(PagedListUtils.mockPagedList(DataDummy.generateDummyCourses()))
            //TestLive.getValue(academyRepository.getAllCourses())

        verify(local).getAllCourses()
        assertNotNull(courseEntities.data)
        assertEquals(courseResponse.size.toLong(), courseEntities.data?.size?.toLong())
    }

    @Test
    fun getBookmarkedCourses() {

        /*
        doAnswer{invocation->
            (invocation.arguments[0] as RemoteDataSource.LoadCourseCallback)
                .onAllCoursesReceived(courseResponse)
            null
        }.`when`(remote).getAllCourses(any())

         */
        val dataSourceFactory= mock(DataSource.Factory::class.java) as DataSource.Factory<Int,CourseEntity>
        //val dummyCourses= MutableLiveData<List<CourseEntity>>()
        //dummyCourses.value=DataDummy.generateDummyCourses()
        `when`(local.getBookmarkedCourses()).thenReturn(dataSourceFactory)
        academyRepository.getBookmarkedCourses()
        val courseEntities =Resource.succes(PagedListUtils.mockPagedList(DataDummy.generateDummyCourses()))
            //TestLive.getValue(academyRepository.getBookmarkedCourses())
        verify(local).getBookmarkedCourses()
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.data?.size?.toLong())
    }

    @Test
    fun getCourseWithModules() {
        /*
        doAnswer{invocation->
            (invocation.arguments[0] as RemoteDataSource.LoadCourseCallback)
                .onAllCoursesReceived(courseResponse)
            null
        }.`when`(remote).getAllCourses(any())

         */
        val dummyEntity=MutableLiveData<CourseWithModules>()
        dummyEntity.value=DataDummy.generateDummyCourseWithModules(DataDummy.generateDummyCourses()[0],false)
        `when`(local.getCourseWithModules(courseId)).thenReturn(dummyEntity)
        val courseEntities=TestLive.getValue(academyRepository.getCourseWithModules(courseId))
        verify(local).getCourseWithModules(courseId)
        assertNotNull(courseEntities.data)
        assertNotNull(courseEntities.data?.mCourseEntity?.title)
        assertEquals(courseResponse[0].title, courseEntities.data?.mCourseEntity?.title)

    }

    @Test
    fun getAllModulesByCourse() {
        /*
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadModulesCallback)
                .onAllModulesReceived(moduleResponses)
            null
        }.`when`(remote).getAllModules(eq(courseId), any())

         */
        val dummyModules=MutableLiveData<List<ModuleEntity>>()
        dummyModules.value= DataDummy.generateDummyModules(courseId)
        `when`(local.getAllModulesByCourse(courseId)).thenReturn(dummyModules)
        var moduleEntities = TestLive.getValue(academyRepository.getAllModulesByCourse(courseId))
        verify(local).getAllModulesByCourse(courseId)
        assertNotNull(moduleEntities.data)
        assertEquals(moduleResponses.size.toLong(), moduleEntities.data?.size?.toLong())
    }

    @Test
    fun getContent() {
        /*
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadModulesCallback)
                .onAllModulesReceived(moduleResponses)
            null
        }.`when`(remote).getAllModules(eq(courseId), any())
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadContentCallback)
                .onContentReceived(content)
            null
        }.`when`(remote).getContent(eq(moduleId), any())

         */
        val dummyEntity=MutableLiveData<ModuleEntity>()
        dummyEntity.value=DataDummy.generateDummyModuleWithContent(moduleId)
        `when`(local.getModuleWithContent(courseId)).thenReturn(dummyEntity)
        val contentEntities = TestLive.getValue(academyRepository.getContent(courseId))
        verify(local).getModuleWithContent(courseId)
        assertNotNull(contentEntities)
        assertNotNull(contentEntities.data?.contentEntity)
        assertNotNull(contentEntities.data?.contentEntity?.content)
        assertEquals(content.content, contentEntities.data?.contentEntity?.content)
    }
}