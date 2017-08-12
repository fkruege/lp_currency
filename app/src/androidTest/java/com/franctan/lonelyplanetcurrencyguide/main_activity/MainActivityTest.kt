package com.franctan.lonelyplanetcurrencyguide.main_activity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.IdRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.franctan.lonelyplanetcurrencyguide.TestApp
import com.franctan.lonelyplanetcurrencyguide.common.SingleLiveEvent
import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.assertion.ViewAssertions.matches
import com.franctan.lonelyplanetcurrencyguide.R
import org.mockito.Mockito.*


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

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
        val testApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
        testApp.daggerComponent().inject(this)
        `when`(viewModelFactory.create(eq(MainViewModel::class.java))).thenReturn(viewModel)

        setupLiveDataObjects()

        activityRule.launchActivity(null)
    }

    @Test
    fun verifyInitialValuesAndThenUpdate() {
        Assert.assertSame(viewModel, activityRule.activity.viewModel)

        verifyPrimaryCurrency()
        verifySecondaryCurrency()

        updatePrimaryCurrency(CurrencyValueModel("PHP", 50.0))
        updateSecondaryCurrency(CurrencyValueModel("CAN", 25.0))

        verifyPrimaryCurrency()
        verifySecondaryCurrency()
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

    fun viewContainsText(@IdRes viewId: Int, text: String) {
        onView(withId(viewId)).check(matches(withText(text)))
    }

    fun setupLiveDataObjects() {
        primaryLiveData.postValue(primary)
        secondaryLiveData.postValue(secondary)

        `when`(viewModel.primaryCurrency).thenReturn(primaryLiveData)
        `when`(viewModel.secondaryCurrency).thenReturn(secondaryLiveData)
        `when`(viewModel.openCurrencyChooserListener()).thenReturn(openCurrencyChooserListener)

    }

}