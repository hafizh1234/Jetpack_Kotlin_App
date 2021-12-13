package com.projek.jetpack.academies.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.projek.jetpack.R
import com.projek.jetpack.academies.data.source.local.entity.CourseEntity
import com.projek.jetpack.academies.viewmodel.ViewModelFactory
import com.projek.jetpack.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment(), BookmarkFragmentCallback {
    private lateinit var viewModel: BookMarkViewModel
    private lateinit var adapterBookmark: BookmarkAdapter
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater)
        return binding.root
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.bindingAdapterPosition
                val courseEntity = adapterBookmark.getSwipedData(swipedPosition)
                courseEntity?.let {
                    viewModel.setBookmark(it)
                }
                val snackBar =
                    Snackbar.make(view as View, R.string.message_undo, Snackbar.LENGTH_LONG)
                snackBar.setAction(
                    R.string.message_ok
                ) {
                    courseEntity?.let { viewModel.setBookmark(it) }
                }

                snackBar.show()
            }
        }

    })

    override fun onShareClick(course: CourseEntity) {
        if (activity != null) {
            val mimeType = "text/plain"
            val shareCompat = ShareCompat.IntentBuilder(requireActivity())
            shareCompat.setType(mimeType)
            shareCompat.setChooserTitle("Bagikan aplikasi ini sekarang.")
            shareCompat.setText(resources.getString(R.string.share_text, course.title))
            shareCompat.startChooser()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouchHelper.attachToRecyclerView(binding.rvBookmark)
        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            //val dataDummy=DataDummy.generateDummyCourses()
            viewModel = ViewModelProvider(
                this,
                factory
            )[BookMarkViewModel::class.java]
            //val dataDummy = viewModel.getAllBookmark()
            adapterBookmark = BookmarkAdapter(this)
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getAllBookmark().observe(viewLifecycleOwner, { course ->
                binding.progressBar.visibility = View.GONE
                adapterBookmark.submitList(course)
                //Toast.makeText(requireContext(),"$course",Toast.LENGTH_SHORT).show()
            })

            //adapter.setCourses(dataDummy)

            with(binding.rvBookmark) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                this.adapter = adapterBookmark
            }
        }
    }
}