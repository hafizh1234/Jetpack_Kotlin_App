package com.projek.jetpack.academies.ui.reader.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.academies.ui.reader.CourseReaderActivity
import com.projek.jetpack.academies.ui.reader.CourseReaderCallback
import com.projek.jetpack.academies.ui.reader.CourseReaderViewModel
import com.projek.jetpack.academies.viewmodel.ViewModelFactory
import com.projek.jetpack.academies.vo.Status
import com.projek.jetpack.databinding.FragmentModuleListBinding

class ModuleListFragment : Fragment(), AdapterClickListener {
    private lateinit var viewModel: CourseReaderViewModel
    private var _binding: FragmentModuleListBinding? = null
    private lateinit var adapter: ModuleListAdapter
    private lateinit var courseReaderCallback: CourseReaderCallback
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ModuleListAdapter(this)
        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[CourseReaderViewModel::class.java]
        //binding.progressBar.visibility=View.VISIBLE
        viewModel.modules.observe(viewLifecycleOwner,{
            moduleEntities->

                if (moduleEntities != null) {
                    when (moduleEntities.status) {
                        Status.LOADING->binding.progressBar.visibility=View.VISIBLE
                        Status.ERROR->{
                            binding.progressBar.visibility=View.GONE
                            Toast.makeText(requireActivity(),"Terjadi kesalahan",Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCES -> if (moduleEntities.data != null) {
                            populateRecyclerview(moduleEntities.data as List<ModuleEntity>)
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }

        })
        /*viewModel.getModules().observe(viewLifecycleOwner, { modules ->
            binding.progressBar.visibility=View.GONE
            populateRecyclerview(modules)
        })
   */
    }

    //context ini sebagai interface dari adapter adapter=ModuleListAdapter(this) shingga, setiap ada click dari adapter akan diteruskan ke sini.
    private fun populateRecyclerview(dummyModules: List<ModuleEntity>) {
        with(binding) {
            progressBar.visibility = View.GONE
            adapter.setModules(dummyModules)
            rvModule.layoutManager = LinearLayoutManager(context)
            rvModule.setHasFixedSize(true)
            rvModule.adapter = adapter
            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            rvModule.addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        courseReaderCallback = context as CourseReaderActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModuleListBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        val TAG: String = ModuleListFragment::class.java.simpleName
        fun newInstance(): ModuleListFragment = ModuleListFragment()
    }

    //sebagai
    override fun onItemClicked(position: Int, moduleId: String) {
        courseReaderCallback.moveTo(position, moduleId)
        viewModel.setSelectedModule(moduleId)
    }

}