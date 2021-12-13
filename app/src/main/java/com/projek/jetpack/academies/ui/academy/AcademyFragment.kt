package com.projek.jetpack.academies.ui.academy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.projek.jetpack.academies.viewmodel.ViewModelFactory
import com.projek.jetpack.academies.vo.Status
import com.projek.jetpack.databinding.FragmentAcademyBinding

class AcademyFragment : Fragment() {
    private var _binding: FragmentAcademyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAcademyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val factory=ViewModelFactory.getInstance(requireActivity())
            //val courses=DataDummy.generateDummyCourses()
            val viewModel = ViewModelProvider(
                this,
                factory
            )[AcademyViewModel::class.java]
            val academyAdapter = AcademyAdapter()
            viewModel.getCourses().observe(viewLifecycleOwner, { courses ->
                if(courses!=null){

                    when(courses.status){
                        Status.LOADING->binding.progressBar.visibility=View.VISIBLE
                        Status.SUCCES->{
                            binding.progressBar.visibility=View.GONE
                            academyAdapter.submitList(courses.data)
                            //academyAdapter.notifyDataSetChanged()
                        }
                        Status.ERROR->{
                            binding.progressBar.visibility=View.GONE
                            Toast.makeText(requireActivity(),"Terjadi kesalahan",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            /*binding.progressBar.visibility=View.GONE
                academyAdapter.setCourses(courses)
                academyAdapter.notifyDataSetChanged()
            })

                 */
            })
            //academyAdapter.setCourses(courses)
            with(binding.rvAcademy) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = academyAdapter
            }
        }
    }
}