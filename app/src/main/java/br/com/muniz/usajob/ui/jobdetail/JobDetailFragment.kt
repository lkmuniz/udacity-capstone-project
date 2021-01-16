package br.com.muniz.usajob.ui.jobdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.muniz.usajob.R
import br.com.muniz.usajob.base.BaseFragment
import br.com.muniz.usajob.databinding.FragmentJobDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [JobDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobDetailFragment() : BaseFragment() {

    private lateinit var binding: FragmentJobDetailBinding

    override val _viewModel: JobDetailViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            JobDetailViewModel.Factory(
                requireActivity().application
            )
        ).get(JobDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_job_detail, container, false
            )

        val job = JobDetailFragmentArgs.fromBundle(requireArguments()).selectedJob
        binding.job = job
        binding.lifecycleOwner = this

        return binding.root
    }

}