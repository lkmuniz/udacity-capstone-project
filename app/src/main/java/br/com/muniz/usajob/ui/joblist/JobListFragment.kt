package br.com.muniz.usajob.ui.joblist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.muniz.usajob.R
import br.com.muniz.usajob.base.BaseFragment
import br.com.muniz.usajob.databinding.FragmentJobListBinding

/**
 * A fragment representing a list of Items.
 */
class JobListFragment : BaseFragment() {

    private lateinit var binding: FragmentJobListBinding

    private val _viewModel: JobListViewModel by lazy {
        ViewModelProvider(this,
            JobListViewModel.Factory(
                requireActivity().application
            )
        ).get(JobListViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_job_list, container, false
            )

        _viewModel.resultJob.observe(viewLifecycleOwner, { newJob ->
            binding.content.text = "Job size: ${newJob.size}"
        })

        binding.fragmentJobGetJob.setOnClickListener {  }
        return binding.root
    }


}