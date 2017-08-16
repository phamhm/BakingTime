package com.example.hp.bakingapp.InstructionActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hp.bakingapp.InstructionActivity.RecyclerViews.InstructionListAdapter;
import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;

public class InstructionListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecipeDatabase db;
    private int recipeId;

    public static String RECIPE_INTENT_KEY = "recipe_intent_key";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public static Intent newInstance(Context context, int recipeID){
        Intent intent = new Intent(context,
                InstructionListActivity.class);

        intent.putExtra(RECIPE_INTENT_KEY, recipeID);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_list);

        setTitle("Instruction");

        if (findViewById(R.id.instruction_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recipeId = getIntent().getIntExtra(RECIPE_INTENT_KEY, -1);
        assert recipeId != -1;
        db = new RecipeDatabase(getApplicationContext());

        RecyclerView instructionsListView = (RecyclerView) findViewById(R.id.instruction_list);
        assert instructionsListView != null;

        FragmentManager fm = getSupportFragmentManager();

        InstructionListAdapter adapter = new InstructionListAdapter(fm, db,recipeId, mTwoPane);
        instructionsListView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        instructionsListView.setLayoutManager(linearLayoutManager);
    }
}
