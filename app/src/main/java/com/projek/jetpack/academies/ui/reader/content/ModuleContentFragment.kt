package com.projek.jetpack.academies.ui.reader.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.databinding.FragmentModuleContentBinding
import com.projek.jetpack.academies.ui.reader.CourseReaderViewModel
import com.projek.jetpack.academies.viewmodel.ViewModelFactory
import com.projek.jetpack.academies.vo.Status

class ModuleContentFragment : Fragment() {
    companion object {
        val TAG: String = ModuleContentFragment::class.java.simpleName
        fun newInstance(): ModuleContentFragment = ModuleContentFragment()
    }
    private lateinit var viewModel:CourseReaderViewModel
    private var _binding: FragmentModuleContentBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
                //binding.progressBar.visibility=View.VISIBLE
            viewModel = ViewModelProvider(requireActivity(), factory)[CourseReaderViewModel::class.java]
            //val module=viewModel.getSelectedModule()
            viewModel.selectedModule.observe(viewLifecycleOwner,{moduleEntitiy->
                if(moduleEntitiy!=null){
                    when(moduleEntitiy.status){
                        Status.LOADING-> binding.progressBar.visibility=View.VISIBLE
                        Status.SUCCES -> if(moduleEntitiy.data!=null){
                            if(moduleEntitiy.data.contentEntity!=null){
                                populateWebView(moduleEntitiy.data)
                            }
                            binding.progressBar.visibility=View.GONE
                            setButtonNextPrevState(moduleEntitiy.data)
                            if(!moduleEntitiy.data.read){
                                viewModel.readContent(moduleEntitiy.data)
                            }
                        }
                        Status.ERROR->{
                            binding.progressBar.visibility=View.GONE
                            Toast.makeText(requireActivity(),"Terjadi kesalahan",Toast.LENGTH_LONG).show()
                        }
                    }
                    binding.btnNext.setOnClickListener{viewModel.setNextPage()}
                    binding.btnPrev.setOnClickListener{viewModel.setPrevPage()}
                }

            })
            /*
            viewModel.getSelectedModule().observe(viewLifecycleOwner, { module ->
                binding.progressBar.visibility=View.GONE
                populateWebView(module)
            })

            //val content = ContentEntity("<h3 class=\\\"fr-text-bordered\\\">Contoh Content</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>")
             */
        }
    }

    private fun setButtonNextPrevState(module: ModuleEntity) {
        if (activity != null) {
            when (module.position) {
                0 -> {
                    binding?.btnPrev?.isEnabled = false
                    binding?.btnNext?.isEnabled = true
                }
                viewModel.getModuleSize() - 1 -> {
                    binding?.btnPrev?.isEnabled = true
                    binding?.btnNext?.isEnabled = false
                }
                else -> {
                    binding?.btnPrev?.isEnabled = true
                    binding?.btnNext?.isEnabled = true
                }
            }
        }
    }

    private fun populateWebView(content: ModuleEntity) {
        binding.webView.loadData(content.contentEntity?.content ?: "", "text/html", "UTF-8")
        //binding.webView.loadData(content.content ?: "", "text/html", "UTF-8")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModuleContentBinding.inflate(inflater)
        return binding.root
    }


}