package br.com.muniz.usajob

object Constants {

    const val DEFAULT = "Default"

    const val BASE_URL = "https://data.usajobs.gov/api/"

    const val WORK_NAME = "RefreshDataWorker"

    const val BASE_IMAGE_URL = "https://www.businessinsider.in/photo/72954472/Get-your-dream-job-with-this-simple-Question.jpg?imgsize=27423"

    // Preferences
    const val DEFAULT_VALUE_INT = 0
    const val LOCATION_PREF_KEY = "location_pref_key"
    const val PAGE_NUMBER_PREF_KEY = "page_number_pref_key"
    const val RESULT_PER_PAGE_PREF_KEY = "result_per_page_pref_key"
    const val DEFAULT_VALUE_STRING = ""
    const val DEFAULT_VALUE_BOOLEAN = false

    // Api query
    const val PAGE_NUMBER = "1"
    const val RESULT_PER_PAGE = "100"
    const val COUNTRY_CODE = "United States"

    const val DEFAULT_COUNTRY = "US"

    // API SERVICE HEADER
    const val HEADER_HOST = "Host: data.usajobs.gov"
    const val HEADER_USER_AGENT = "User-Agent: xxxxx" //TODO FILL WITH YOUR EMAIL
    const val HEADER_AUTHORIZATION_KEY = "Authorization-Key: xxxx" // TODO FILL WITH AUTHORIZATION KEY
}