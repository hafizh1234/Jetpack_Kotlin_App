package com.projek.jetpack.academies.ui.reader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.projek.jetpack.academies.data.source.local.entity.ContentEntity
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.utils.DataDummy
import com.projek.jetpack.academies.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CourseReaderViewModelTest {
    private lateinit var viewModel: CourseReaderViewModel

    private var courseChosen = DataDummy.generateDummyCourses()[0]
    private var courseId = courseChosen.courseId
    private var modules = DataDummy.generateDummyModules(courseId)
    private var moduleId = modules[0].moduleId

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var academyRepository: AcademyRepository

    @Mock
    private lateinit var moduleObserver: Observer<Resource<List<ModuleEntity>>>

    @Mock
    private lateinit var contentObserver: Observer<Resource<ModuleEntity>>

    @Test
    fun getModules() {
        val modulesLive = MutableLiveData<Resource<List<ModuleEntity>>>()
        val resources=Resource.succes(modules) as Resource<List<ModuleEntity>>
        modulesLive.value = resources
        `when`(academyRepository.getAllModulesByCourse(courseId)).thenReturn(modulesLive)
        //val moduleEntities = viewModel.getModules().value
        //verify(academyRepository).getAllModulesByCourse(courseId)
        //assertNotNull(moduleEntities)
        //assertEquals(7, moduleEntities?.size)
        //val observer= mock(Observer::class.java) as Observer<Resource<List<ModuleEntity>>
        viewModel.modules.observeForever(moduleObserver)
        verify(moduleObserver).onChanged(modulesLive.value)
    }

    @Test
    fun getSelectedModule() {
        val module = MutableLiveData<Resource<ModuleEntity>>()
        var resources = Resource.succes(modules[0])
        module.value=resources
        `when`(academyRepository.getContent(moduleId)).thenReturn(module)
        //val moduleEntities = viewModel.getSelectedModule().value as ModuleEntity
        //verify(academyRepository).getContent(courseId, moduleId)
        //assertNotNull(moduleEntities)
        //val contentEntity = moduleEntities.contentEntity
        //assertNotNull(contentEntity)
        //val content = contentEntity?.content
        //assertNotNull(content)
        //assertEquals(content, modules[0].contentEntity?.content)
        viewModel.selectedModule.observeForever(contentObserver)
        verify(contentObserver).onChanged(resources)
    }

    @Before
    fun set() {
        viewModel = CourseReaderViewModel(academyRepository)

        viewModel.setSelectedCourse(courseId)
        viewModel.setSelectedModule(moduleId)
        val dummyModule = modules[0]
        dummyModule.contentEntity =
            ContentEntity("<h3 class=\\\"fr-text-bordered\\\">" + dummyModule.title + "</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>")

    }
}