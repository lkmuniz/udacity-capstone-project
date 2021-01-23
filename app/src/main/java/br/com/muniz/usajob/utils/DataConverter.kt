package br.com.muniz.usajob.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.muniz.usajob.base.BaseRecyclerViewAdapter
import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.JobEntity
import br.com.muniz.usajob.data.local.subdivision.Subdivision
import br.com.muniz.usajob.data.local.subdivision.SubdivisionEntity

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
            jobCategory = it.jobCategory,
            jobQualificationSummary = it.jobQualificationSummary,
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
            jobCategory = it.jobCategory,
            jobQualificationSummary = it.jobQualificationSummary,
            jobMinimumRange = it.jobMinimumRange,
            jobMaximumRange = it.jobMaximumRange,
            jobRateIntervalCode = it.jobRateIntervalCode
        )
    }
}

fun List<Subdivision>.subdivisionAsDatabaseModel(): List<SubdivisionEntity> {
    return map {
        SubdivisionEntity(
            name = it.name
        )
    }
}

fun List<SubdivisionEntity>.subdivisionAsNameList(): List<String> {
    return map {
        it.name
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