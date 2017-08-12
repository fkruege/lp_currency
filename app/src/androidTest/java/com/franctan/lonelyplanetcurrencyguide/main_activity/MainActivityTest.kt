package com.franctan.lonelyplanetcurrencyguide.main_activity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProvider
import android.content.pm.ActivityInfo
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.franctan.lonelyplanetcurrencyguide.R
import com.franctan.lonelyplanetcurrencyguide.TestApp
import com.franctan.lonelyplanetcurrencyguide.common.SingleLiveEvent
import com.franctan.lonelyplanetcurrencyguide.main_activity.MainViewModel.WHICH_CURRENCY.*
import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel
import com.franctan.lonelyplanetcurrencyguide.test_support.EspressoTestHelper.Companion.clickBtn
import com.franctan.lonelyplanetcurrencyguide.test_support.EspressoTestHelper.Companion.clearAndTypeString
import com.franctan.lonelyplanetcurrencyguide.test_support.EspressoTestHelper.Companion.viewContainsText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import javax.inject.Inject
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.junit.Assert.assertSame


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    val PRIMARY_TEXT_ID = R.id.txtPrimaryCurrency
    val SECONDARY_TEXT_ID = R.id.txtSecondaryCurrency

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java, true, false)

    @Inject lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var primary = CurrencyValueModel("USD", 1.00)
    val primaryLiveData: MutableLiveData<CurrencyValueModel> = MutableLiveData()

    var secondary = CurrencyValueModel("EUR", 1.25)
    val secondaryLiveData: MutableLiveData<CurrencyValueModel> = MutableLiveData()

    val openCurrencyChooserListener = SingleLiveEvent<Void>()

    @Before
    fun setUp() {
        mockViewModel()
        setupLiveDataCurrencyModels()
        activityRule.launchActivity(null)
    }

    @Test
    fun verifyPrimaryAndSecondaryCurrencyValues() {
        verifyPrimaryCurrency()
        verifySecondaryCurrency()

        updatePrimaryCurrency(CurrencyValueModel("PHP", 50.0))
        updateSecondaryCurrency(CurrencyValueModel("CAN", 25.0))

        verifyPrimaryCurrency()
        verifySecondaryCurrency()
    }

    @Test
    fun verifyPrimaryBtnClick() {
        clickBtn(R.id.btnPrimaryCurrency)
        verify(viewModel, times(1)).openCurrencyChooserFor(eq(PRIMARY))
    }

    @Test
    fun verifySecondaryBtnClick() {
        clickBtn(R.id.btnSecondaryCurrency)
        verify(viewModel, times(1)).openCurrencyChooserFor(eq(SECONDARY))
    }

    @Test
    fun verifyPrimaryTextChangeUpdatesModel() {
        val text = "11.55"
        clearAndTypeString(PRIMARY_TEXT_ID, text)
        verify(viewModel, times(1)).primaryCurrencyChanged(eq("1"))
        verify(viewModel, times(1)).primaryCurrencyChanged(eq("11"))
        verify(viewModel, times(1)).primaryCurrencyChanged(eq("11."))
        verify(viewModel, times(1)).primaryCurrencyChanged(eq("11.5"))
        verify(viewModel, times(1)).primaryCurrencyChanged(eq("11.55"))
    }

    @Test
    fun verifySecondaryTextChangeUpdatesModel() {
        val text = "11.55"
        clearAndTypeString(SECONDARY_TEXT_ID, text)
        verify(viewModel, times(1)).secondaryCurrencyChanged(eq("1"))
        verify(viewModel, times(1)).secondaryCurrencyChanged(eq("11"))
        verify(viewModel, times(1)).secondaryCurrencyChanged(eq("11."))
        verify(viewModel, times(1)).secondaryCurrencyChanged(eq("11.5"))
        verify(viewModel, times(1)).secondaryCurrencyChanged(eq("11.55"))
    }

    @Test
    fun verifyOrientationChanges_UseSameModel() {
        assertSame(viewModel, activityRule.activity.viewModel)
        activityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        assertSame(viewModel, activityRule.activity.viewModel)
    }


    private fun verifyPrimaryCurrency() {
        viewContainsText(R.id.btnPrimaryCurrency, primary.currency)
        viewContainsText(R.id.txtPrimaryCurrency, primary.valueAsString())
    }

    private fun verifySecondaryCurrency() {
        viewContainsText(R.id.btnSecondaryCurrency, secondary.currency)
        viewContainsText(R.id.txtSecondaryCurrency, secondary.valueAsString())
    }

    private fun updatePrimaryCurrency(model: CurrencyValueModel) {
        primary = model
        primaryLiveData.postValue(primary)
    }

    private fun updateSecondaryCurrency(model: CurrencyValueModel) {
        secondary = model
        secondaryLiveData.postValue(secondary)
    }

    fun mockViewModel() {
        val testApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        testApp.daggerComponent().inject(this)
        `when`(viewModelFactory.create(eq(MainViewModel::class.java))).thenReturn(viewModel)
    }

    fun setupLiveDataCurrencyModels() {
        primaryLiveData.postValue(primary)
        secondaryLiveData.postValue(secondary)

        `when`(viewModel.primaryCurrency).thenReturn(primaryLiveData)
        `when`(viewModel.secondaryCurrency).thenReturn(secondaryLiveData)
        `when`(viewModel.openCurrencyChooserListener()).thenReturn(openCurrencyChooserListener)
    }

}