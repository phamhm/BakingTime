package com.example.hp.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hp.bakingapp.RecyclerViews.RecyclerAdapter;
import com.example.hp.bakingapp.Utilities.NetworkUtility;
import com.example.hp.bakingapp.Utilities.Recipe;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerAdapter mAdapter;
    private RecipeDatabase db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Baking Recipes");

        mAdapter = new RecyclerAdapter();

        if (savedInstanceState == null){
            // load data from the internet again
            db = new RecipeDatabase(this);
            db.cleanupData();
            List<Recipe> recipeList = NetworkUtility.getRecipesFromServer(getApplicationContext());
            db.insertRecipeFromList(recipeList);
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipe_names);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter.updateData(db);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.updateData(db);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.updateData(db);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
