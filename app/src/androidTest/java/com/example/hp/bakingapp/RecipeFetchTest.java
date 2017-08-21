package com.example.hp.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.hp.bakingapp.Utilities.NetworkUtility;
import com.example.hp.bakingapp.Utilities.Recipe;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeFetchTest {
    List<Recipe> recipes;

    @Before
    public void setup(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        NetworkUtility.getRecipesFromServer(null, null);
    }

    @Test
    public void checkRecipeCount(){
        Assert.assertNotNull("Network fetch not successful", recipes);
        Assert.assertEquals(recipes.size(),4);
    }
}
