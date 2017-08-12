package com.franctan.lonelyplanetcurrencyguide

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import com.franctan.lonelyplanetcurrencyguide.main_activity.CurrencyCalculator
import com.franctan.lonelyplanetcurrencyguide.main_activity.MainViewModel
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.*


import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val mock = mock(CurrencyCalculator::class.java)
        `when`(mock.dummyMock()).thenReturn(false)
    }

    @Test
    fun testFactory() {
        val vm = mock(MainViewModel::class.java)
        val factory = mock(ViewModelProvider.Factory::class.java)
        `when`(factory.create(eq(MainViewModel::class.java))).thenReturn(vm)
        val result = factory.create(MainViewModel::class.java)
        assertSame(result, vm)
    }



}
