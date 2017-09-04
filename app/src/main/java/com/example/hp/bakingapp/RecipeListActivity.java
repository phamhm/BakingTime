package com.example.hp.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hp.bakingapp.RecyclerViews.RecyclerAdapter;
import com.example.hp.bakingapp.Utilities.NetworkUtility;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerAdapter mAdapter;
    private RecipeDatabase db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Baking Recipes");

        mAdapter = new RecyclerAdapter();
        reloadDb();

        RecyclerView mRecyclerView = findViewById(R.id.rv_recipe_names);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        NetworkUtility.getRecipesFromServer(mAdapter, db);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadDb();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reloadDb();
    }

    private void reloadDb(){
        if(db == null){
            db = new RecipeDatabase(this);
            mAdapter.updateData(db);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null)
          db.close();
    }
}
