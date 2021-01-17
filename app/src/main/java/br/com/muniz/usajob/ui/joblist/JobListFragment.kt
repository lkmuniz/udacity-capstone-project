package br.com.muniz.usajob.ui.joblist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.muniz.usajob.R
import br.com.muniz.usajob.base.BaseFragment
import br.com.muniz.usajob.base.NavigationCommand
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.databinding.FragmentJobListBinding
import br.com.muniz.usajob.utils.setup

/**
 * A fragment representing a list of Items.
 */
class JobListFragment : BaseFragment() {

    private lateinit var binding: FragmentJobListBinding

    override val _viewModel: JobListViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
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

        binding.viewModel = _viewModel

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            _viewModel.clearAndRefreshDataBase()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = JobAdapter { job ->
            navigateToAddReminder(job)
        }

        // setup the recycler view using the extension function
        binding.jobFragmentList.setup(adapter)
    }

    private fun navigateToAddReminder(job: Job) {
        //use the navigationCommand live data to navigate between the fragments
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                JobListFragmentDirections.actionJobListFragmentToJobDetailFragment(job)
            )
        )
    }
}