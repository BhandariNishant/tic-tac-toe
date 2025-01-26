package androidsamples.java.tictactoe;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;

import androidx.test.espresso.contrib.RecyclerViewActions;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;


import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleInstrumentedTest {

    @BeforeClass
    public static void enableAccessibilityChecks() {
        // Enable accessibility checks
        AccessibilityChecks.enable().setRunChecksFromRootView(true);

    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("androidsamples.java.tictactoe", appContext.getPackageName());
    }

    @Test
    public void test_1_successfulLogin() throws InterruptedException {
        // Enter valid email and password
        onView(withId(R.id.edit_email)).perform(typeText("validuser@gmail.com"));
        onView(withId(R.id.edit_password)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.btn_log_in)).perform(click());
        sleep(5000);
        // Check if dashboard is displayed
        onView(withId(R.id.heading)).check(matches(isDisplayed()));
    }
    @Test
    public void test_2_NewGameDialogAppears() {
        // Assuming user is already logged in
        onView(withId(R.id.fab_new_game)).perform(click());
        onView(withText(R.string.new_game)).check(matches(isDisplayed()));
        onView(withText(R.string.two_player)).check(matches(isDisplayed()));
        onView(withText(R.string.one_player)).check(matches(isDisplayed()));
    }
    @Test
    public void test_3_DoublePlayerGame() {
        onView(withId(R.id.fab_new_game)).perform(click());
        onView(withText(R.string.two_player)).perform(click());
        onView(withId(R.id.list)).check(new RecyclerViewItemCountAssertion(1));
        }
    @Test
    public void test_4_SinglePlayerNav(){
        onView(withId(R.id.fab_new_game)).perform(click());
        // Click on the negative button, which corresponds to "One Player" mode
        onView(withText(R.string.one_player)).perform(click());

        // Check that the single player game fragment is displayed
        onView(withId(R.id.gameFragmentSingle))
                .check(matches(isDisplayed()));
    }


    @Test
    public void test_6_DoublePlayerNav(){
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.gameFragmentDouble)).check(matches(isDisplayed()));
    }



    public static class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assert adapter != null;
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }
}

//    @Test
//    public void test_5_SinglePlayerGamePlay() throws InterruptedException {
//        onView(withId(R.id.fab_new_game)).perform(click());
//        onView(withText(R.string.one_player)).perform(click());
//        onView(withId(R.id.gameFragmentSingle))
//                .check(matches(isDisplayed()));
//        sleep(5000);
//        onView(withId(R.id.button1)).perform(click());
//        onView(withId(R.id.button1)).check(matches(withText("X")));
//    }

//    @Test
//    public void test_7_DoublePlayerForfeit(){
//        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//        pressBack();
//        onView(withText(R.string.confirm)).check(matches(isDisplayed()));
//        onView(withText(R.string.forfeit_game_dialog_message)).check(matches(isDisplayed()));
//
//    }



