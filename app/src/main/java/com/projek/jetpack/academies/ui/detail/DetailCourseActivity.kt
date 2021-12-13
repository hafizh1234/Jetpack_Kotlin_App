package com.projek.jetpack.academies.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.projek.jetpack.R
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.ui.reader.CourseReaderActivity
import com.projek.jetpack.academies.viewmodel.ViewModelFactory
import com.projek.jetpack.academies.vo.Status
import com.projek.jetpack.databinding.ActivityDetailCourseBinding
import com.projek.jetpack.databinding.ContentDetailCourseBinding

class DetailCourseActivity : AppCompatActivity() {
    private lateinit var activityBinding:ActivityDetailCourseBinding
    private lateinit var binding: ContentDetailCourseBinding
    private lateinit var viewModel:DetailCourseViewModel
    private var menu:Menu?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDetailCourseBinding.inflate(layoutInflater)
        binding = activityBinding.detailContent

        setContentView(activityBinding.root)
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailCourseViewModel::class.java]

        setSupportActionBar(activityBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val adapter = DetailCourseAdapter()

        val extras = intent.extras
        if (extras != null) {
            val courseId = extras.getString(EXTRA_COURSE)
            if (courseId != null) {
                viewModel.setSelectedCourse(courseId)
                //val modules=viewModel.getModules()
                //val modules = DataDummy.generateDummyModules(courseId)
                //binding.progressBar.visibility = View.VISIBLE
                viewModel.courseModule.observe(this, { courseWithModule ->

                        if (courseWithModule != null) {
                            when (courseWithModule.status) {
                                Status.LOADING -> binding.progressBar.visibility=View.VISIBLE
                                Status.SUCCES->if(courseWithModule.data!=null){
                                    binding.progressBar.visibility=View.GONE
                                    activityBinding.detailContent.rvModule.visibility=View.VISIBLE
                                    adapter.setModules(courseWithModule.data.mModules)
                                    adapter.notifyDataSetChanged()
                                    populateCourse(courseWithModule.data.mCourseEntity)
                                }
                                Status.ERROR->{
                                    binding.progressBar.visibility=View.GONE
                                    Toast.makeText(this,"Terjadi kesalahan",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    })

                /*
viewModel.getModules().observe(this,{modules->
    binding.progressBar.visibility=View.GONE
    adapter.setModules(modules)
    adapter.notifyDataSetChanged()
})
viewModel.getCourse().observe(this,{ courses->
    populateCourse(courses)
})
//adapter.setModules(modules)
//populateCourse(viewModel.getCourse())

 */
            }
        }

        with(binding.rvModule) {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@DetailCourseActivity)
            setHasFixedSize(true)
            this.adapter = adapter
            val dividerItemDecoration =
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun populateCourse(courseEntity: CourseEntity) {
        binding.textTitle.text = courseEntity.title
        binding.textDescription.text = courseEntity.description
        binding.textDate.text = resources.getString(R.string.deadline_date, courseEntity.deadline)

        Glide.with(this)
            .load(courseEntity.imagePath)
            .transform(RoundedCorners(20))
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.error)
            )
            .into(binding.imagePoster)

        binding.btnStart.setOnClickListener {
            val intent = Intent(this@DetailCourseActivity, CourseReaderActivity::class.java)
            intent.putExtra(CourseReaderActivity.EXTRA_COURSE_ID, courseEntity.courseId)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_COURSE = "extra_course"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail,menu)
        this.menu=menu
        viewModel.courseModule.observe(this,{
            courseModule->
            if(courseModule!=null){
                when(courseModule.status){
                    Status.LOADING->binding.progressBar.visibility=View.VISIBLE
                    Status.SUCCES->if(courseModule.data!=null){
                        binding.progressBar.visibility=View.GONE
                        val state=courseModule.data.mCourseEntity.bookmarked
                        setBookmarked(state)
                    }
                    Status.ERROR->{
                        binding.progressBar.visibility=View.VISIBLE
                        Toast.makeText(this,"Terjadi Kesalahan",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.action_bookmark){
            viewModel.setBookmark()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setBookmarked(state: Boolean) {
        if(menu==null) return
        val menuItem=menu?.findItem(R.id.action_bookmark)
        if(state){
            menuItem?.icon=ContextCompat.getDrawable(this,R.drawable.ic_bookmarked_white)
        }else{
            menuItem?.icon=ContextCompat.getDrawable(this,R.drawable.ic_bookmark_white)
        }
    }
}