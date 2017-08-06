package com.franctan.lonelyplanetcurrencyguide.main_activity

import android.support.test.runner.AndroidJUnit4
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class DummyTest {

    @Test
    fun test1() {
        val mock = mock(Dummy::class.java)
        `when`(mock.test()).thenReturn(false)
        assertFalse(mock.test())
    }

}