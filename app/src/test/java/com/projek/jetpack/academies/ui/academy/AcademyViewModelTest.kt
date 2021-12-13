package com.projek.jetpack.academies.ui.academy

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AcademyViewModelTest {
    private lateinit var viewModel: AcademyViewModel
    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @Mock
    private lateinit var academyRepository: AcademyRepository
    @Mock
    private lateinit var observer: Observer<Resource<PagedList<CourseEntity>>>

    @Mock
    private lateinit var pagdList:PagedList<CourseEntity>
    @Test
    fun getCourses() {
        val dummyCourses = Resource.succes(pagdList)
        `when`(dummyCourses.data?.size).thenReturn(5)
        val courses = MutableLiveData<Resource<PagedList<CourseEntity>>>()
        courses.value = dummyCourses

        `when`(academyRepository.getAllCourses()).thenReturn(courses)
        val courseEntities = viewModel.getCourses().value?.data
        verify(academyRepository).getAllCourses()
        assertNotNull(courseEntities)
        assertEquals(5, courseEntities?.size)

        viewModel.getCourses().observeForever(observer)
        verify(observer).onChanged(dummyCourses)
    }

    @Before
    fun setup() {
        viewModel = AcademyViewModel(academyRepository)
    }
}