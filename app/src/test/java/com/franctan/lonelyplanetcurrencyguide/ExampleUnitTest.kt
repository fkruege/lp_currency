package com.franctan.lonelyplanetcurrencyguide

import android.app.Application
import com.franctan.lonelyplanetcurrencyguide.main_activity.CurrencyCalculator
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
}
