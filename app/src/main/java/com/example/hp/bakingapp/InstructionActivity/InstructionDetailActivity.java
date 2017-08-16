package com.example.hp.bakingapp.InstructionActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.hp.bakingapp.InstructionActivity.RecyclerViews.InstructionListAdapter;
import com.example.hp.bakingapp.R;

/**
 * An activity representing a single Instruction detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link InstructionListActivity}.
 */
public class InstructionDetailActivity extends AppCompatActivity {
    private static String RECIPE_ITEM_ID = "recipe_id";
    private static String SELECT_ITEM_TYPE = "select-item-type";
    private static String STEP_POSITION = "step_position";


    public static Intent newInstance(Context context,
                                     int recipeId,
                                     int itemViewType,
                                     int position){
        Intent intent = new Intent(context, InstructionDetailActivity.class);

        intent.putExtra(RECIPE_ITEM_ID, recipeId);
        intent.putExtra(SELECT_ITEM_TYPE, itemViewType);
        intent.putExtra(STEP_POSITION, position);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            int itemViewType = getIntent().getIntExtra(SELECT_ITEM_TYPE,
                    InstructionListAdapter.POSITION_INSTRUCTIONS);
            int recipeId = getIntent().getIntExtra(RECIPE_ITEM_ID,-1);
            int position = getIntent().getIntExtra(STEP_POSITION, -1);

            Fragment fragment;
            if (itemViewType == InstructionListAdapter.POSITION_INGREDIENTS){
                fragment = IngredientFragment.newInstance(recipeId);
            } else {
                int stepId = position - 1;
                fragment = InstructionDetailFragment.newInstance(recipeId,stepId);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.instruction_detail_container, fragment)
                    .commit();
        }
    }
}
