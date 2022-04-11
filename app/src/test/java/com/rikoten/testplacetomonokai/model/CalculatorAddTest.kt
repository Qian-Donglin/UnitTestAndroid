package com.rikoten.testplacetomonokai.model

import com.google.common.truth.Truth
import org.junit.Assert.*

import org.junit.Test

class CalculatorAddTest {

    @Test
    fun add1() {
        Truth.assertThat(Calculator.add(
            2, 3
        )).apply {
            isEqualTo(5)
        }
    }

    @Test
    fun add2() {
        Truth.assertThat(Calculator.add(
            3, 2
        )).apply {
            isEqualTo(5)
        }
    }

    @Test
    fun add3() {
        Truth.assertThat(Calculator.add(
            0, 3
        )).apply {
            isEqualTo(3)
        }
    }

    @Test
    fun add4() {
        Truth.assertThat(Calculator.add(
            -2, 3
        )).apply {
            isEqualTo(1)
        }
    }
}