package br.com.muniz.usajob.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.muniz.usajob.base.BaseRecyclerViewAdapter
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.JobEntity

fun List<Job>.asDatabaseModel(): List<JobEntity> {
    return map {
        JobEntity(
            id = it.id,
            applyUri = it.applyUri,
            locationName = it.locationName,
            country = it.country,
            countrySubDivisionCode = it.countrySubDivisionCode,
            longitude = it.longitude,
            latitude = it.latitude,
            organizationName = it.organizationName,
            jobName = it.jobName,
            jobMinimumRange = it.jobMinimumRange,
            jobMaximumRange = it.jobMaximumRange,
            jobRateIntervalCode = it.jobRateIntervalCode
        )
    }
}

fun List<JobEntity>.asDomainModel(): List<Job> {
    return map {
        Job(
            id = it.id,
            applyUri = it.applyUri,
            locationName = it.locationName,
            country = it.country,
            countrySubDivisionCode = it.countrySubDivisionCode,
            longitude = it.longitude,
            latitude = it.latitude,
            organizationName = it.organizationName,
            jobName = it.jobName,
            jobMinimumRange = it.jobMinimumRange,
            jobMaximumRange = it.jobMaximumRange,
            jobRateIntervalCode = it.jobRateIntervalCode
        )
    }
}

fun <T> RecyclerView.setup(
    adapter: BaseRecyclerViewAdapter<T>
) {
    this.apply {
        layoutManager = LinearLayoutManager(this.context)
        this.adapter = adapter
    }
}