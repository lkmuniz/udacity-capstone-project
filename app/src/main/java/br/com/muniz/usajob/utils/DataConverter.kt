package br.com.muniz.usajob.utils

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
            latitude = it.latitude
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
            latitude = it.latitude
        )
    }
}