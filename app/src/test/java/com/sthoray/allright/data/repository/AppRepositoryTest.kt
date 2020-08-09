package com.sthoray.allright.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sthoray.allright.data.api.RetrofitInstance
import com.sthoray.allright.data.db.SearchHistoryDatabase
import com.sthoray.allright.data.model.listing.Listing
import com.sthoray.allright.data.model.main.FeatureCategoriesResponse
import com.sthoray.allright.data.model.search.SearchResponse
import com.sthoray.allright.ui.listing.viewmodel.ListingViewModel
import com.sthoray.allright.utils.Internet
import com.sthoray.allright.utils.Resource
import com.sthoray.allright.utils.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.rules.TestRule
import retrofit2.Response


class AppRepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var historyDatabase: SearchHistoryDatabase
    @RelaxedMockK
    private lateinit var featureCategoriesResponse: FeatureCategoriesResponse
    @RelaxedMockK
    private lateinit var secondTierCategoriesResponse : List<Category>
    @RelaxedMockK
    private lateinit var searchListingResponse: SearchResponse
    @RelaxedMockK
    private lateinit var getListingResponse: Listing

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(Internet)
    }
    @Test
    fun getFeaturedCategories() =
        mainCoroutineRule.runBlockingTest {
            val appRepository = AppRepository(historyDatabase)
            val response = Response.success(featureCategoriesResponse)

            coEvery { RetrofitInstance.api.getFeatureCategories() } returns response
            appRepository.getFeatureCategories()
            coVerify { RetrofitInstance.api.getFeatureCategories() }

            /*val testId = 9
            val errorResponse: Response<Listing> = Response.error(
                400,
                "{\"key\":[\"some_stuff\"]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )

            every { Internet.hasConnection(any()) } returns true
            coEvery { appRepository.getListing(eq(testId)) } returns errorResponse

            val listingViewModel = ListingViewModel(app, appRepository)
            listingViewModel.getListing(testId)

            verify { Internet.hasConnection(any()) }

            Truth.assertThat(listingViewModel.listing.value).isInstanceOf(Resource.Error::class.java)
            Truth.assertThat(listingViewModel.listing.value?.message).isEqualTo(errorResponse.message())*/
        }

    @Test
    fun getSecondTierCategories() {
    }

    @Test
    fun searchListings() {
    }

    @Test
    fun getListing() {
    }
}