package com.example.hp.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.hp.bakingapp.Utilities.Ingredient;
import com.example.hp.bakingapp.Utilities.NetworkUtility;
import com.example.hp.bakingapp.Utilities.Recipe;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;
import com.example.hp.bakingapp.Utilities.Step;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by hp on 8/11/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeSqliteTest {
    RecipeDatabase db;

    @Before
    public void setup(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        List<Recipe> recipes = NetworkUtility.getRecipesFromServer(appContext);

        db = new RecipeDatabase(appContext);
        for(Recipe recipe : recipes){
            db.insertRecipe(recipe);
        }
    }

    @After
    public void cleanup(){
        db.cleanupData();
        db.close();
    }

    @Test
    public void testDbNotNull(){
        Assert.assertNotNull(db);
    }

    @Test
    public void testRecipesCount(){
        Assert.assertEquals(4, db.getRecipeCount());
    }

    @Test
    public void testFetchRecipeById(){
        Recipe recipe = db.getRecipeById(1);

        Assert.assertNotNull(recipe);
        Assert.assertEquals("Nutella Pie", recipe.getName());
        Assert.assertEquals("Graham Cracker crumbs",
                recipe.getIngredients().get(0).getIngredient());

    }

    @Test
    public void testFetchIngredientByRecipeId(){
        List<Ingredient> ingredient = db.getIngredientListByRecipeId(1);

        Assert.assertNotNull(ingredient);

        Assert.assertEquals("Graham Cracker crumbs", ingredient.get(0).getIngredient());
    }

    @Test
    public void testFetchStepByRecipeId(){
        List<Step> steps = db.getStepListByRecipeId(1);

        Assert.assertNotNull(steps);
        Assert.assertEquals(7, steps.size());

        Assert.assertEquals("Recipe Introduction", steps.get(0).getShortDescription());
    }
}
