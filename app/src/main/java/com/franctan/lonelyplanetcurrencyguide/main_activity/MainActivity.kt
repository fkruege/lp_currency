package com.franctan.lonelyplanetcurrencyguide.main_activity

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.franctan.lonelyplanetcurrencyguide.R
import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleRegistryOwner, HasSupportFragmentInjector {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    lateinit var primaryTextWatcher: TextWatcher
    lateinit var secondaryTextWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        setContentView(R.layout.activity_main)

        initializeViews()
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    fun initializeViews() {
        observeViewModels()
        setListenerEvents()
        initTextWatchers()
    }

    fun observeViewModels() {
        observePrimaryCurrency()
        observeSecondaryCurrency()
    }

    fun observePrimaryCurrency() {
        viewModel.primaryCurrency.observe(this, Observer<CurrencyValueModel> { currencyModel ->
            btnPrimaryCurrency.text = currencyModel!!.currency
            val saveIndex = txtPrimaryCurrency.selectionStart
            txtPrimaryCurrency.setText(currencyModel!!.valueAsString(), TextView.BufferType.EDITABLE)
            txtPrimaryCurrency.setSelection(saveIndex)
        })
    }


    fun observeSecondaryCurrency() {
        viewModel.secondaryCurrency.observe(this, Observer<CurrencyValueModel> { currencyModel ->
            btnSecondaryCurrency.text = currencyModel!!.currency
            val saveIndex = txtSecondaryCurrency.selectionStart
            txtSecondaryCurrency.setText(currencyModel!!.valueAsString(), TextView.BufferType.EDITABLE)
            txtSecondaryCurrency.setSelection(saveIndex)
        })
    }

    fun setListenerEvents() {
        setPrimaryCurrency_InputEvents()
        setSecondaryCurrency_InputEvents()
        listenForChooseCurrencyEvent()
    }

    fun setPrimaryCurrency_InputEvents() {
        btnPrimaryCurrency.setOnClickListener {
            viewModel.openCurrencyChooserFor(MainViewModel.WHICH_CURRENCY.PRIMARY)
        }

    }

    fun setSecondaryCurrency_InputEvents() {
        btnSecondaryCurrency.setOnClickListener {
            viewModel.openCurrencyChooserFor(MainViewModel.WHICH_CURRENCY.SECONDARY)
        }
    }

    fun initTextWatchers(){
        createWatchers()
        addPrimaryTextWatcher()
        addSecondaryTextWatcher()
    }


    fun addPrimaryTextWatcher(){
       txtPrimaryCurrency.addTextChangedListener(primaryTextWatcher)
    }

    fun addSecondaryTextWatcher(){
        txtSecondaryCurrency.addTextChangedListener(secondaryTextWatcher)
    }

    fun createWatchers() {

        primaryTextWatcher = object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(input: Editable?) {
                viewModel.primaryCurrencyChanged(input.toString())
            }
        }

        secondaryTextWatcher = object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(input: Editable?) {
                viewModel.secondaryCurrencyChanged(input.toString())
            }
        }

    }

    fun listenForChooseCurrencyEvent() {
        viewModel.openCurrencyChooserListener().observe(this, Observer<Void> {
            val fragment: ChooseCurrencyDialogFragment = ChooseCurrencyDialogFragment.create()
            fragment.show(supportFragmentManager, fragment.tag)
        })
    }

}
