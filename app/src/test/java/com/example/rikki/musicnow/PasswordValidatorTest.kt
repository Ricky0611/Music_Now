package com.example.rikki.musicnow

import com.example.rikki.musicnow.utils.Constants.passwordPattern
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun passwordValidator_MatchPasswordPattern_ReturnTrue() {
        assertTrue("Matched password failed!", passwordPattern.matcher("Test0123").matches())
    }

    @Test
    fun passwordValidator_NoUpperCase_ReturnFalse() {
        assertFalse("Password without upper cases passed!", passwordPattern.matcher("test0123").matches())
    }

    @Test
    fun passwordValidator_NoLowerCase_ReturnFalse() {
        assertFalse("Password without lower cases passed!", passwordPattern.matcher("TEST0123").matches())
    }

    @Test
    fun passwordValidator_NoDigit_ReturnFalse() {
        assertFalse("Password without digits passed!", passwordPattern.matcher("TESTtest").matches())
    }

    @Test
    fun passwordValidator_LessThan_ReturnFalse() {
        assertFalse("Password less than 6 characters passed!", passwordPattern.matcher("Test0").matches())
    }

    @Test
    fun passwordValidator_MoreThan_ReturnFalse() {
        assertFalse("Password more than 10 characters passed!", passwordPattern.matcher("Test0123456").matches())
    }
}
