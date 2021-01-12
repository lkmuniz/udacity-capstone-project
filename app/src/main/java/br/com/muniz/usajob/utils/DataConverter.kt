package br.com.muniz.usajob.utils

import br.com.muniz.usajob.data.Job
import br.com.muniz.usajob.data.local.JobEntity

fun List<Job>.asDatabaseModel(): List<JobEntity> {
    return map {
        JobEntity(
            id = it.id,
            Code = it.Code,
            Value = it.Value,
            LastModified = it.LastModified,
            IsDisabled = it.IsDisabled
        )
    }
}