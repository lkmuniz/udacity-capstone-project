package br.com.muniz.usajob.ui.jobdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.muniz.usajob.R
import br.com.muniz.usajob.base.BaseFragment
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.databinding.FragmentJobDetailBinding
import br.com.muniz.usajob.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 * Use the [JobDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobDetailFragment() : BaseFragment() {

    private lateinit var binding: FragmentJobDetailBinding
    private lateinit var job: Job

    override val _viewModel by inject<JobDetailViewModel>()

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

        job = JobDetailFragmentArgs.fromBundle(requireArguments()).selectedJob
        binding.job = job
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        binding.detailApply.setOnClickListener {
            openApplyUrl()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_route -> {
            openRoute()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun openRoute() {
        val gmmIntentUri = Uri.parse("google.navigation:q=${job.latitude},${job.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            requireActivity().startActivity(mapIntent)
        }
    }

    private fun openApplyUrl() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(job.applyUri.replace("\"", ""))

        requireActivity().startActivity(intent)

    }
}