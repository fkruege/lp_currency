package com.franctan.lonelyplanetcurrencyguide.main_activity

import android.arch.lifecycle.*
import android.arch.lifecycle.ViewModelProvider.Factory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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
    lateinit var viewModelFactory: Factory
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

    override fun onResume() {
        super.onResume()
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
        createObservers()
        addPrimaryObserver()
        addSecondaryObserver()
        hookPrimaryFocusEventsToObserver()
        hookSecondaryFocusEventsToObserver()
    }

    fun createObservers() {

        primaryCurrencyObserver = Observer<CurrencyValueModel> { model ->
            if (model != null && model != txtPrimaryCurrency.tag) {
                btnPrimaryCurrency.text = model.currency
                setText(model.valueAsString(), txtPrimaryCurrency)
                txtPrimaryCurrency.tag = model
            }
        }


        secondaryCurrencyObserver = Observer<CurrencyValueModel> { model ->
            if (model != null && model != txtSecondaryCurrency.tag) {
                btnSecondaryCurrency.text = model.currency
                setText(model.valueAsString(), txtSecondaryCurrency)
                txtSecondaryCurrency.tag = model
            }
        }
    }

    fun setText(newValue: String, editText: EditText) {
        val saveIndex = editText.selectionStart
        editText.setText(newValue, TextView.BufferType.EDITABLE)
        if (saveIndex >= 0 && saveIndex < newValue.length) {
            editText.setSelection(saveIndex)
        }
    }

    lateinit var primaryCurrencyObserver: Observer<CurrencyValueModel>
    lateinit var secondaryCurrencyObserver: Observer<CurrencyValueModel>

    fun hookPrimaryFocusEventsToObserver() {
        txtPrimaryCurrency.onFocusChangeListener =
                View.OnFocusChangeListener { p0, hasFocus ->
                    if (hasFocus) {
                        removePrimaryObserver()
                    } else {
                        addPrimaryObserver()
                    }
                }
    }


    fun hookSecondaryFocusEventsToObserver() {
        txtSecondaryCurrency.onFocusChangeListener =
                View.OnFocusChangeListener { p0, hasFocus ->
                    if (hasFocus) {
                        removeSecondaryObserver()
                    } else {
                        addSecondaryObserver()
                    }
                }
    }

    fun addPrimaryObserver() {
        viewModel.primaryCurrency.observe(this@MainActivity, primaryCurrencyObserver)
    }

    fun addSecondaryObserver() {
        viewModel.secondaryCurrency.observe(this, secondaryCurrencyObserver)
    }

    fun removePrimaryObserver() {
        viewModel.primaryCurrency.removeObserver(primaryCurrencyObserver)
    }


    fun removeSecondaryObserver() {
        viewModel.secondaryCurrency.removeObserver(secondaryCurrencyObserver)
    }


    fun setListenerEvents() {
        setPrimaryCurrency_InputEvents()
        setSecondaryCurrency_InputEvents()
        listenForChooseCurrencyEvent()
    }

    fun setPrimaryCurrency_InputEvents() {
        btnPrimaryCurrency.isFocusable = true
        btnPrimaryCurrency.setOnClickListener {
            txtPrimaryCurrency.clearFocus()
            btnPrimaryCurrency.requestFocus()
            viewModel.openCurrencyChooserFor(MainViewModel.WHICH_CURRENCY.PRIMARY)
        }

    }

    fun setSecondaryCurrency_InputEvents() {
        btnSecondaryCurrency.isFocusable = true
        btnSecondaryCurrency.setOnClickListener {
            txtSecondaryCurrency.clearFocus()
            btnSecondaryCurrency.requestFocus()
            viewModel.openCurrencyChooserFor(MainViewModel.WHICH_CURRENCY.SECONDARY)
        }
    }

    fun initTextWatchers() {
        createWatchers()
        addPrimaryTextWatcher()
        addSecondaryTextWatcher()
    }


    fun addPrimaryTextWatcher() {
        txtPrimaryCurrency.addTextChangedListener(primaryTextWatcher)
    }

    fun addSecondaryTextWatcher() {
        txtSecondaryCurrency.addTextChangedListener(secondaryTextWatcher)
    }

    fun createWatchers() {

        primaryTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(input: Editable?) {
                viewModel.primaryCurrencyChanged(input.toString())
            }
        }

        secondaryTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
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
