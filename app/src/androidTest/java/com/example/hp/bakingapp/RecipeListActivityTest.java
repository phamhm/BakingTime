package com.example.hp.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.hp.bakingapp.InstructionActivity.InstructionListActivity.RECIPE_INTENT_KEY;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by hp on 8/21/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {
    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityRule = new IntentsTestRule<>(
            RecipeListActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testRecipeSelection(){
        ViewInteraction recipeRecyclerView = onView(withId(R.id.rv_recipe_names));

        // i know that there are at least 4 recipe.
        // it's better to check the size, but whatever for now.
        for(int i = 0; i < 4; i++){
            recipeRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            intended(allOf(
                    hasExtra(RECIPE_INTENT_KEY, i + 1)));
            pressBack();
        }
    }

    @Test
    public void testInstruction(){
        ViewInteraction recipeRecyclerView = onView(withId(R.id.rv_recipe_names));

        // i know that there are at least 4 recipe.
        // it's better to check the size, but whatever for now.
        recipeRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(allOf(hasExtra(RECIPE_INTENT_KEY, 1)));

        ViewInteraction instructionListView = onView(withId(R.id.instruction_list));
        instructionListView.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        pressBack();
    }
}
