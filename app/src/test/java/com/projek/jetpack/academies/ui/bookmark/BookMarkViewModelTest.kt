package com.projek.jetpack.academies.ui.bookmark

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.data.source.AcademyRepository
import com.projek.jetpack.academies.utils.DataDummy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookMarkViewModelTest {
    private lateinit var viewModel: BookMarkViewModel

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @Mock
    private lateinit var observer:Observer<PagedList<CourseEntity>>
    @Mock
    private lateinit var academyRepository: AcademyRepository
    @Mock
    private lateinit var pagedList:PagedList<CourseEntity>
    @Before
    fun setUp() {
        viewModel = BookMarkViewModel(academyRepository)
    }

    @Test
    fun getBookmark() {
        val dummyCourses=pagedList
        `when`(dummyCourses.size).thenReturn(5)
        val courses=MutableLiveData<PagedList<CourseEntity>>()
        courses.value=dummyCourses
        `when`(academyRepository.getBookmarkedCourses())
            .thenReturn(courses)
        val bookmark = viewModel.getAllBookmark().value
        verify(academyRepository).getBookmarkedCourses()
        assertNotNull(bookmark)
        assertEquals(5, bookmark?.size)
        viewModel.getAllBookmark().observeForever(observer)
        verify(observer).onChanged(dummyCourses)
    }
}