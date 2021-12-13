package com.projek.jetpack.academies.ui.academy

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.projek.jetpack.R
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.databinding.ItemsAcademyBinding
import com.projek.jetpack.academies.ui.detail.DetailCourseActivity

class AcademyAdapter:PagedListAdapter<CourseEntity,AcademyAdapter.CourseViewHolder>(DIFF_CALLBACK) {
    companion object{
        private val DIFF_CALLBACK=object:DiffUtil.ItemCallback<CourseEntity>(){
            override fun areContentsTheSame(oldItem: CourseEntity, newItem: CourseEntity): Boolean {
                return oldItem==newItem
            }

            override fun areItemsTheSame(oldItem: CourseEntity, newItem: CourseEntity): Boolean {
                return oldItem.courseId==newItem.courseId
            }
        }
    }

    private var listCourses=ArrayList<CourseEntity>()
    inner class CourseViewHolder(private val binding:ItemsAcademyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(courseEntity: CourseEntity){
            with(binding){
                tvItemTitle.text=courseEntity.title
                tvItemDate.text=itemView.resources.getString(R.string.deadline_date,courseEntity.deadline)
                itemView.setOnClickListener{
                    val intent= Intent(itemView.context, DetailCourseActivity::class.java)
                    intent.putExtra(DetailCourseActivity.EXTRA_COURSE, courseEntity.courseId)
                    itemView.context.startActivity(intent)
                }
                Glide.with(itemView.context)
                    .load(courseEntity.imagePath)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.error)
                    .into(imgPoster)
            }
        }
    }

    fun setCourses(courses:List<CourseEntity>?){
        if(courses==null) return
        this.listCourses.clear()
        this.listCourses.addAll(courses)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemsAcademyBinding=ItemsAcademyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CourseViewHolder(itemsAcademyBinding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course=getItem(position)
        if (course != null) {
            holder.bind(course)
        }
    }
/*
    override fun getItemCount(): Int {
        return listCourses.size
    }

 */
}