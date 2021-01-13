package br.com.muniz.usajob.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Job(
    val id: Long,
    val applyUri: String,
    val locationName: String,
    val country: String,
    val countrySubDivisionCode: String,
    val longitude: String,
    val latitude: String,
) : Parcelable