package br.com.muniz.usajob.data.remote

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubdivisionResult(
    @Json(name = "CodeList")
    val codeList: List<CodeList>?,

    @Json(name = "DateGenerated")
    val dateGenerated: String?

) : Parcelable

@Parcelize
data class CodeList(
    @Json(name = "ValidValue")
    val validValue: List<ValidValue>?,

    @Json(name = "id")
    val id: String?
) : Parcelable

@Parcelize
data class ValidValue(
    @Json(name = "Code")
    val code: String?,

    @Json(name = "Value")
    val value: String?,

    @Json(name = "ParentCode")
    val parentCode: String?,

    @Json(name = "LastModified")
    val lastModified: String?,

    @Json(name = "IsDisabled")
    val isDisabled: String?
) : Parcelable





