package br.com.muniz.usajob.data.remote

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class JobResult(

    @Json(name = "LanguageCode")
    val languageCode: String?,

    @Json(name = "SearchResult")
    val searchResult: SearchResult?
) : Parcelable


@Parcelize
data class SearchResult(

    @Json(name = "SearchResultCount")
    val searchResultCount: Long?,

    @Json(name = "SearchResultCountAll")
    val searchResultCountAll: Long?,

    @Json(name = "SearchResultItems")
    val searchResultItems: List<SearchResultItem>?,

    @Json(name = "UserArea")
    val userArea: SearchResultUserArea?
) : Parcelable

@Parcelize
data class SearchResultItem(
    @Json(name = "MatchedObjectId")
    val matchedObjectID: String?,

    @Json(name = "MatchedObjectDescriptor")
    val matchedObjectDescriptor: MatchedObjectDescriptor?,

    @Json(name = "RelevanceRank")
    val relevanceRank: Double?
) : Parcelable

@Parcelize
data class MatchedObjectDescriptor(
    @Json(name = "PositionID")
    val positionID: String?,

    @Json(name = "PositionTitle")
    val positionTitle: String?,

    @Json(name = "PositionURI")
    val positionURI: String?,

    @Json(name = "ApplyURI")
    val applyURI: List<String>?,

    @Json(name = "PositionLocation")
    val positionLocation: List<PositionLocation>?,

    @Json(name = "OrganizationName")
    val organizationName: String?,

    @Json(name = "DepartmentName")
    val departmentName: String?,

    @Json(name = "JobCategory")
    val jobCategory: List<JobCategory>?,

    @Json(name = "JobGrade")
    val jobGrade: List<JobGrade>?,

    @Json(name = "PositionSchedule")
    val positionSchedule: List<JobCategory>?,

    @Json(name = "PositionOfferingType")
    val positionOfferingType: List<JobCategory>?,

    @Json(name = "QualificationSummary")
    val qualificationSummary: String?,

    @Json(name = "PositionRemuneration")
    val positionRemuneration: List<PositionRemuneration>?,

    @Json(name = "PositionStartDate")
    val positionStartDate: String?,

    @Json(name = "PositionEndDate")
    val positionEndDate: String?,

    @Json(name = "PublicationStartDate")
    val publicationStartDate: String?,

    @Json(name = "ApplicationCloseDate")
    val applicationCloseDate: String?,

    @Json(name = "PositionFormattedDescription")
    val positionFormattedDescription: List<PositionFormattedDescription>?,

    @Json(name = "UserArea")
    val userArea: MatchedObjectDescriptorUserArea?
) : Parcelable

@Parcelize
data class JobCategory(
    @Json(name = "Name")
    val name: String?,

    @Json(name = "Code")
    val code: String?
) : Parcelable

@Parcelize
data class JobGrade(
    @Json(name = "Code")
    val code: String?
) : Parcelable

@Parcelize
data class PositionFormattedDescription(
    @Json(name = "Content")
    val content: String?,

    @Json(name = "Label")
    val label: String?,

    @Json(name = "LabelDescription")
    val labelDescription: String?
) : Parcelable

@Parcelize
data class PositionLocation(
    @Json(name = "LocationName")
    val locationName: String?,

    @Json(name = "CountryCode")
    val countryCode: String?,

    @Json(name = "CountrySubDivisionCode")
    val countrySubDivisionCode: String?,

    @Json(name = "CityName")
    val cityName: String?,

    @Json(name = "Longitude")
    val longitude: Double?,

    @Json(name = "Latitude")
    val latitude: Double?
) : Parcelable

@Parcelize
data class PositionRemuneration(
    @Json(name = "MinimumRange")
    val minimumRange: String?,

    @Json(name = "MaximumRange")
    val maximumRange: String?,

    @Json(name = "RateIntervalCode")
    val rateIntervalCode: String?
) : Parcelable

@Parcelize
data class MatchedObjectDescriptorUserArea(
    @Json(name = "Details")
    val details: Details?,

    @Json(name = "IsRadialSearch")
    val isRadialSearch: Boolean?
) : Parcelable

@Parcelize
data class Details(
    @Json(name = "MajorDuties")
    val majorDuties: String?,

    @Json(name = "Education")
    val education: String?,

    @Json(name = "Requirements")
    val requirements: String?,

    @Json(name = "Evaluations")
    val evaluations: String?,

    @Json(name = "HowToApply")
    val howToApply: String?,

    @Json(name = "WhatToExpectNext")
    val whatToExpectNext: String?,

    @Json(name = "RequiredDocuments")
    val requiredDocuments: String?,

    @Json(name = "Benefits")
    val benefits: String?,

    @Json(name = "BenefitsUrl")
    val benefitsURL: String?,

    @Json(name = "OtherInformation")
    val otherInformation: String?,

    @Json(name = "KeyRequirements")
    val keyRequirements: @RawValue List<Any>?,

    @Json(name = "JobSummary")
    val jobSummary: String?,

    @Json(name = "WhoMayApply")
    val whoMayApply: JobCategory?,

    @Json(name = "LowGrade")
    val lowGrade: String?,

    @Json(name = "HighGrade")
    val highGrade: String?,

    @Json(name = "SubAgencyName")
    val subAgencyName: String?,

    @Json(name = "OrganizationCodes")
    val organizationCodes: String?
) : Parcelable

@Parcelize
data class SearchResultUserArea(
    @Json(name = "Refiners")
    val refiners: Refiners?,

    @Json(name = "NumberOfPages")
    val numberOfPages: String?,

    @Json(name = "IsRadialSearch")
    val isRadialSearch: Boolean?
) : Parcelable

@Parcelize
data class Refiners(
    @Json(name = "Organization")
    val organization: List<GradeBucket>?,

    @Json(name = "GradeBucket")
    val gradeBucket: List<GradeBucket>?,

    @Json(name = "SalaryBucket")
    val salaryBucket: List<GradeBucket>?,

    @Json(name = "PositionOfferingTypeCode")
    val positionOfferingTypeCode: List<GradeBucket>?,

    @Json(name = "PositionScheduleTypeCode")
    val positionScheduleTypeCode: List<GradeBucket>?,

    @Json(name = "JobCategoryCode")
    val jobCategoryCode: List<GradeBucket>?
) : Parcelable

@Parcelize
data class GradeBucket(
    @Json(name = "RefinementName")
    val refinementName: String?,

    @Json(name = "RefinementCount")
    val refinementCount: String?,

    @Json(name = "RefinementToken")
    val refinementToken: String?,

    @Json(name = "RefinementValue")
    val refinementValue: String?
) : Parcelable
