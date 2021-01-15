package br.com.muniz.usajob.ui.joblist

import br.com.muniz.usajob.R
import br.com.muniz.usajob.base.BaseRecyclerViewAdapter
import br.com.muniz.usajob.data.Job

//Use data binding to show the job on the item
class JobAdapter(callBack: (job: Job) -> Unit) :
    BaseRecyclerViewAdapter<Job>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.job_item
}