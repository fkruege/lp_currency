package com.franctan.lonelyplanetcurrencyguide.main_activity;


import com.franctan.lonelyplanetcurrencyguide.common.SingleLiveEvent;
import com.franctan.lonelyplanetcurrencyguide.models.CurrencyConversionRatesModel;
import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel;
import com.franctan.lonelyplanetcurrencyguide.repositories.CurrencyRepository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.franctan.lonelyplanetcurrencyguide.main_activity.MainViewModel.WHICH_CURRENCY.PRIMARY;
import static com.franctan.lonelyplanetcurrencyguide.models.ConstantsKt.DEFAULT_CURRENCY;
import static com.franctan.lonelyplanetcurrencyguide.models.ConstantsKt.DEFAULT_VALUE;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    public enum WHICH_CURRENCY {
        PRIMARY, SECONDARY
    }

    private WHICH_CURRENCY currencyToggle = PRIMARY;

    private MutableLiveData<CurrencyValueModel> baseCurrency;

    private MutableLiveData<CurrencyValueModel> primaryCurrency;

    private MutableLiveData<CurrencyValueModel> secondaryCurrency;


    private final SingleLiveEvent<Void> openCurrencyChooserEvent = new SingleLiveEvent<>();


    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final CurrencyRepository currencyRepository;

    private final CurrencyCalculator calculator;

    private final LinkedHashMap<String, CurrencyValueModel> currencyMap = new LinkedHashMap<>();

    private final List<String> currencyList = new ArrayList<>();

    @Inject
    public MainViewModel(CurrencyRepository currencyRepository, CurrencyCalculator calculator) {
        this.currencyRepository = currencyRepository;
        this.calculator = calculator;

        init();
    }

    private void init() {
        initializeBaseAndOtherCurrencies();
        getCurrencyRatesFromRepository();
    }

    private void initializeBaseAndOtherCurrencies() {
        CurrencyValueModel defaultCurrency = new CurrencyValueModel();
        this.baseCurrency = new MutableLiveData<>();
        this.baseCurrency.setValue(defaultCurrency);

        this.primaryCurrency = new MutableLiveData<>();
        this.primaryCurrency.setValue(defaultCurrency);

        this.secondaryCurrency = new MutableLiveData<>();
        this.secondaryCurrency.setValue(defaultCurrency);
    }

    private void getCurrencyRatesFromRepository() {
        currencyRepository
                .getConversionRatesFor(DEFAULT_CURRENCY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CurrencyConversionRatesModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(CurrencyConversionRatesModel currencyConversionRatesModel) {
                        populateCurrencyModels(currencyConversionRatesModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error getting currencies", e);
                    }
                });
    }

    private void populateCurrencyModels(CurrencyConversionRatesModel currencyConversionRatesModel) {
        populateCurrencyMap(currencyConversionRatesModel);
        populateCurrencyList();
        setSecondaryCurrency();
    }


    private void populateCurrencyMap(CurrencyConversionRatesModel currencyConversionRatesModel) {
        HashMap<String, Double> ratesMap = currencyConversionRatesModel.getRatesMap();
        addBaseCurrency(currencyConversionRatesModel, ratesMap);

        List<String> sortedKeys = new ArrayList<>(ratesMap.keySet());
        Collections.sort(sortedKeys);

        for (String key : sortedKeys) {
            CurrencyValueModel model = new CurrencyValueModel(key, ratesMap.get(key));
            currencyMap.put(key, model);
        }
    }

    private void addBaseCurrency(CurrencyConversionRatesModel currencyConversionRatesModel, HashMap<String, Double> ratesMap) {
        ratesMap.put(currencyConversionRatesModel.getBaseCurrency(), DEFAULT_VALUE);
    }

    private void populateCurrencyList() {
        Set<String> currencyKeys = currencyMap.keySet();
        currencyList.addAll(currencyKeys);
    }

    private void setSecondaryCurrency() {
        if (!currencyList.isEmpty()) {
            CurrencyValueModel secondaryModel = currencyMap.get(currencyList.get(0));
            secondaryCurrency.setValue(secondaryModel);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public void openCurrencyChooserFor(WHICH_CURRENCY whichOne) {
        currencyToggle = whichOne;
        openCurrencyChooserEvent.call();
    }

    public SingleLiveEvent<Void> openCurrencyChooserListener() {
        return openCurrencyChooserEvent;
    }

    public void chooseCurrency(String currency) {
        CurrencyValueModel model = currencyMap.get(currency);
        if (currencyToggle == PRIMARY) {
            CurrencyEqualizer equalizer = getCurrencyEqualizerWithNewPrimary(model);
            CurrencyValueModel newPrimary = equalizer.equalizeUsingSecondary(getSecondaryCurrency().getValue().getValue());
            primaryCurrency.setValue(newPrimary);
        } else {
            CurrencyEqualizer equalizer = getCurrencyEqualizerWithNewSecondary(model);
            CurrencyValueModel newSecondary = equalizer.equalizeUsingPrimary(getPrimaryCurrency().getValue().getValue());
            secondaryCurrency.setValue(newSecondary);
        }
    }

    public int getCurrentIndexForSelectedCurrency() {
        String key;
        if (currencyToggle == PRIMARY) {
            key = primaryCurrency.getValue().getCurrency();
        } else {
            key = secondaryCurrency.getValue().getCurrency();
        }
        return currencyList.indexOf(key);
    }


    public MutableLiveData<CurrencyValueModel> getPrimaryCurrency() {
        return primaryCurrency;
    }

    public void primaryCurrencyChanged(String input) {
        try {
            double newValue = Double.parseDouble(input);
            updatePrimary(newValue);

            if (currencyMap.size() == 0) {
                return;
            }

            CurrencyEqualizer equalizer = getCurrencyEqualizer();
            CurrencyValueModel updated = equalizer.equalizeUsingPrimary(newValue);
            if (!calculator.areEqual(secondaryCurrency.getValue().getValue(), updated.getValue())) {
                updateSecondary(updated);
            }

        } catch (Exception ex) {
            Log.e(TAG, "Primary Invalid input entered: " + input, ex);
        }
    }


    public void secondaryCurrencyChanged(String input) {
        try {
            double newValue = Double.parseDouble(input);
            updateSecondary(newValue);

            if (currencyMap.size() == 0) {
                return;
            }

            CurrencyEqualizer equalizer = getCurrencyEqualizer();
            CurrencyValueModel updated = equalizer.equalizeUsingSecondary(newValue);
            if (!calculator.areEqual(primaryCurrency.getValue().getValue(), updated.getValue())) {
                updatePrimary(updated);
            }


        } catch (Exception ex) {
            Log.e(TAG, "Secondary Invalid input entered: " + input, ex);
        }
    }

    private void updatePrimary(double newValue){
        CurrencyValueModel current = primaryCurrency.getValue();
        updatePrimary(current.setValue(newValue));
    }


    private void updateSecondary(double newValue){
        CurrencyValueModel current = secondaryCurrency.getValue();
        updateSecondary(current.setValue(newValue));
    }

    private void updatePrimary(CurrencyValueModel model){
       primaryCurrency.setValue(model);
    }


    private void updateSecondary(CurrencyValueModel model){
       secondaryCurrency.setValue(model);
    }

    public MutableLiveData<CurrencyValueModel> getSecondaryCurrency() {
        return secondaryCurrency;
    }

    public List<String> getCurrencyList() {
        return currencyList;
    }

    private CurrencyEqualizer getCurrencyEqualizerWithNewPrimary(CurrencyValueModel newPrimary) {
        CurrencyValueModel secondary = getBaseSecondary();
        CurrencyEqualizer equalizer = new CurrencyEqualizer(baseCurrency.getValue(), newPrimary, secondary, calculator);
        return equalizer;
    }


    private CurrencyEqualizer getCurrencyEqualizerWithNewSecondary(CurrencyValueModel newSecondary) {
        CurrencyValueModel primary = getBasePrimary();
        CurrencyEqualizer equalizer = new CurrencyEqualizer(baseCurrency.getValue(), primary, newSecondary, calculator);
        return equalizer;
    }


    private CurrencyEqualizer getCurrencyEqualizer() {
        CurrencyValueModel primary = getBasePrimary();
        CurrencyValueModel secondary = getBaseSecondary();
        CurrencyEqualizer equalizer = new CurrencyEqualizer(baseCurrency.getValue(), primary, secondary, calculator);
        return equalizer;
    }

    private CurrencyValueModel getBasePrimary() {
        String key = primaryCurrency.getValue().getCurrency();
        return currencyMap.get(key);
    }

    private CurrencyValueModel getBaseSecondary() {
        String key = secondaryCurrency.getValue().getCurrency();
        return currencyMap.get(key);
    }


}
