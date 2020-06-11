package srg.pmd;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MarioSpec01 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void jumpingMario() {
        ViewInteraction mushroom = onView(allOf(withContentDescription("mushroom_main")));
        mushroom.perform(click());

        ViewInteraction mario = onView(allOf(withContentDescription("mario_small")));
        Matcher scoreMatcher = allOf(withContentDescription("score"));
        ViewInteraction score = onView(scoreMatcher);
        score.check(matches(withText("0")));
        mario.perform(click());
        waitForScore(scoreMatcher, "1");
        mario.perform(click());
        waitForScore(scoreMatcher, "2");
        mario.perform(click());
        waitForScore(scoreMatcher, "3");
    }

    @Test
    public void verifyStartScreen() {
        ViewInteraction imageView = onView(
            allOf(withContentDescription("mushroom_main"),
                childAtPosition(
                    allOf(withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0)),
                    2),
                isDisplayed()));
    }


    @Test
    public void verifyHelloText() {
        ViewInteraction TextView = onView(
            allOf(withContentDescription("start_text"),
                childAtPosition(
                    allOf(withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0)),
                    2),
                isDisplayed()));
    }


    @Test
    public void textPresentShouldFail() {
        ViewInteraction imageView = onView(
            allOf(withContentDescription("mushroom_main"),
                childAtPosition(
                    allOf(withId(R.id.main_layout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0)),
                    2),
                isDisplayed()));
    }


    private static boolean doesScoreMatch(Matcher m, String expected) {
        return allOf(m, withText(expected)).matches(m);
    }

    private static boolean waitForScore(Matcher m, String expected) {
        int MAX_ATTEMPTS = 10;
        for (int i=0; i<MAX_ATTEMPTS; i++) {
            if ( doesScoreMatch(m, expected) ) {
                return true;
            } else {
                try {
                    Thread.sleep(200);
                } catch ( InterruptedException e ) {
                    break;
                }
            }
        }
        return false;
    }

    private static Matcher<View> childAtPosition(
        final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                    && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}