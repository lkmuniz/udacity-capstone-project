package br.com.muniz.usajob.ui.joblist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import br.com.muniz.usajob.R
import br.com.muniz.usajob.base.BaseFragment
import br.com.muniz.usajob.databinding.FragmentJobListBinding

/**
 * A fragment representing a list of Items.
 */
class JobListFragment : BaseFragment() {

    private lateinit var binding: FragmentJobListBinding

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

        return binding.root
    }
}