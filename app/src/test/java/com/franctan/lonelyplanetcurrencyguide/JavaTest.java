package com.franctan.lonelyplanetcurrencyguide;


import com.franctan.lonelyplanetcurrencyguide.main_activity.MainViewModel;
import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel;

import org.junit.Test;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JavaTest {

    @Test
    public void testFactory() {

        MainViewModel vm = mock(MainViewModel.class);
        ViewModelProvider.Factory factory = mock(ViewModelProvider.Factory.class);

        when(factory.create(eq(MainViewModel.class))).thenReturn(vm);

        MainViewModel result = factory.create(MainViewModel.class);
        assertSame(result, vm);

        when(vm.getPrimaryCurrency()).thenReturn(new MutableLiveData<CurrencyValueModel>());

    }


}
