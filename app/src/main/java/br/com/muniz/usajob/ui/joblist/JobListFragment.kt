package br.com.muniz.usajob.ui.joblist

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.muniz.usajob.R
import br.com.muniz.usajob.authentication.AuthenticationActivity
import br.com.muniz.usajob.base.BaseFragment
import br.com.muniz.usajob.base.NavigationCommand
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.databinding.FragmentJobListBinding
import br.com.muniz.usajob.utils.cancelNotifications
import br.com.muniz.usajob.utils.setDisplayHomeAsUpEnabled
import br.com.muniz.usajob.utils.setup
import com.firebase.ui.auth.AuthUI
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A fragment representing a list of Items.
 */
class JobListFragment : BaseFragment() {

    private lateinit var binding: FragmentJobListBinding

    override val _viewModel by inject<JobListViewModel>()

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
        setHasOptionsMenu(true)
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            _viewModel.clearAndRefreshDataBase()
        }

        setupSubdivisionSpinner()
        setDisplayHomeAsUpEnabled(false)
        cancelNotifications(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)

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

    private fun setupSubdivisionSpinner() {
        _viewModel.resultSubdivision.observe(viewLifecycleOwner, { countryList ->
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                countryList
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerSubdivision.setAdapter(adapter)

                /**
                 * Check if the user already selected some location and set on spinner
                 * */
                val prefLocation = _viewModel.getPrefLocation()
                if (prefLocation.isNotEmpty()) {
                    var pos = _viewModel.resultSubdivision.value?.indexOf(prefLocation)
                    pos = if (pos == -1) 0 else pos
                    binding.spinnerSubdivision.hint = _viewModel.resultSubdivision.value?.get(pos!!)
                    _viewModel.showSnackBar.value = pos.toString()
                } else {
                    binding.spinnerSubdivision.hint = context?.getString(R.string.select_yout_city)
                }
            }
        })

        /**
         *  When user select the division from spinner, the sharedpreference is updated
         *  and a new query is made
         */
        binding.spinnerSubdivision.setOnItemClickListener { adapterView, view, position, l ->
            val value = _viewModel.resultSubdivision.value?.get(position)
            _viewModel.saveLocationPreference(value)

        }
    }

    private fun logout() {
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            _viewModel.logoutFlow()
            _viewModel.showToast.value = context?.getString(R.string.logout_successfully)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }
}