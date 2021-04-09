package com.example.yummlyteam.yummly_project;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yummlyteam.app.MainActivity;
import com.example.yummlyteam.app.Util;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {
  @Rule
  public ActivityTestRule<MainActivity> activityTestRule =
          new ActivityTestRule<>(MainActivity.class);

  @Test
  public void testSearchAndScroll() {
    // Context of the app under test.
    Util.areMocksEnabled = true;

    //FUTURE WORK: Using underlying edittext due to Espresso not supporting SearchView actions
    //Proper way would be to implement two custom matchers for search view (current method would fail if there were two search views)
    Espresso.onView(ViewMatchers.withId(R.id.search_src_text)).perform(ViewActions.typeText("hui"));
    Espresso.onView(ViewMatchers.withId(R.id.search_src_text)).perform(ViewActions.pressImeActionButton());
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Espresso.onView(ViewMatchers.withSubstring("Twice Cooked Pork")).check(matches(isDisplayed()));
    Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(scrollToBottomAction());
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollTo(ViewMatchers.hasDescendant(ViewMatchers.withSubstring("Huli Huli Ribs"))));
  }
  private static ViewAction scrollToBottomAction() {
    return new ViewAction() {
      @Override
      public Matcher<View> getConstraints() {
        return allOf(isDisplayed(), isAssignableFrom(RecyclerView.class));
      }

      @Override
      public String getDescription() {
        return "scroll recyclerview to bottom";
      }

      @Override
      public void perform(UiController uiController, View view) {
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.smoothScrollBy(0, recyclerView.computeVerticalScrollRange()+1);
        uiController.loopMainThreadUntilIdle();
      }
    };
  }
}
