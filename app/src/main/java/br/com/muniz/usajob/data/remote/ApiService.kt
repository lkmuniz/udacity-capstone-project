package br.com.muniz.usajob.data.remote

import br.com.muniz.usajob.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiService {
    @Headers(
        Constants.HEADER_HOST,
        Constants.HEADER_USER_AGENT,
        Constants.HEADER_AUTHORIZATION_KEY
    )
    /**
     *  The API is not filtering the LocationName correctly
     */
    @GET("search?")
    fun getJobs(
        @Query("LocationName") locationName: String,
        @Query("Page") page: String,
        @Query("ResultsPerPage") resultsPerPage: String,
        @Query("Keyword") keyword: String,
        @Query("Fields") fields: String = "min"
    ): Deferred<JobResult>

    @Headers(
        Constants.HEADER_HOST,
        Constants.HEADER_USER_AGENT,
        Constants.HEADER_AUTHORIZATION_KEY
    )
    @GET("codelist/countrysubdivisions/")
    fun getSubdivision(): Deferred<String>
}

private fun client(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {

    /**
     * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
     * object.
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val jobs = retrofit.create(ApiService::class.java)
}