package com.projek.jetpack.academies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.data.source.local.entity.CourseWithModules
import com.projek.jetpack.academies.utils.DataDummy
import com.projek.jetpack.academies.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailCourseViewModelTest {
    private lateinit var viewModel: DetailCourseViewModel
    private var dummyCourse = DataDummy.generateDummyCourses()[0]
    private var idCourse = dummyCourse.courseId
    private var dummyModules=DataDummy.generateDummyModules(idCourse)

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @Mock
    private lateinit var observer:Observer<Resource<CourseWithModules>>
    @Mock
    private lateinit var courseObserver: Observer<CourseEntity>
    @Mock
    private lateinit var moduleObserver:Observer<List<ModuleEntity>>
    @Mock
    private lateinit var academyRepository: AcademyRepository

    @Before
    fun set() {
        viewModel = DetailCourseViewModel(academyRepository)
        viewModel.setSelectedCourse(idCourse)
    }

    @Test
    fun getCourseWithModules(){
        val dummyCourseWithModules=Resource.succes(DataDummy.generateDummyCourseWithModules(dummyCourse,true))
        val course= MutableLiveData<Resource<CourseWithModules>>()
        course.value=dummyCourseWithModules

        `when`(academyRepository.getCourseWithModules(idCourse)).thenReturn(course)
        viewModel.courseModule.observeForever(observer)
        verify(observer).onChanged(dummyCourseWithModules)
    }
/*
    @Test
    fun getCourse() {
        val course= MutableLiveData<CourseEntity>()
        course.value=dummyCourse
        `when`(academyRepository.getCourseWithModules(idCourse)).thenReturn(course)
        //viewModel.setSelectedCourse(dummyCourse.courseId)
        val courseEntity = viewModel.getCourse().value as CourseEntity
        verify(academyRepository).getCourseWithModules(idCourse)
        assertNotNull(courseEntity)
        assertEquals(courseEntity.courseId, dummyCourse.courseId)
        assertEquals(courseEntity.deadline, dummyCourse.deadline)
        assertEquals(dummyCourse.description, courseEntity.description)
        assertEquals(dummyCourse.imagePath, courseEntity.imagePath)
        assertEquals(dummyCourse.title, courseEntity.title)
        viewModel.getCourse().observeForever(courseObserver)
        verify(courseObserver).onChanged(dummyCourse)
    }

    @Test
    fun getModules() {
        val modules=MutableLiveData<List<ModuleEntity>>()
        modules.value=dummyModules
        `when`(academyRepository.getAllModulesByCourse(idCourse)).thenReturn(modules
        )
        val moduleEntities = viewModel.getModules().value
        verify(academyRepository).getAllModulesByCourse(idCourse)
        assertNotNull(moduleEntities)
        assertEquals(7, moduleEntities?.size)
        viewModel.getModules().observeForever(moduleObserver)
        verify(moduleObserver).onChanged(dummyModules)
    }

*/
}