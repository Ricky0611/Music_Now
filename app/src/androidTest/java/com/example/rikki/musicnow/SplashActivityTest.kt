package com.example.rikki.musicnow

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<SplashActivity>()

    @Test
    fun checkTextViewContent() {
        onView(ViewMatchers.withId(R.id.fullscreen_content)).check(matches(withText(R.string.app_name)))
    }

    @Test
    fun checkButtonContent() {
        onView(ViewMatchers.withId(R.id.start)).check(matches(withText(R.string.start_now)))
    }
}
