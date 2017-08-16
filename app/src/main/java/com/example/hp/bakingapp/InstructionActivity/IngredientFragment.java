package com.example.hp.bakingapp.InstructionActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.bakingapp.InstructionActivity.RecyclerViews.MyIngredientRecyclerViewAdapter;
import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.Ingredient;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>

 */
public class IngredientFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String RECIPE_ID = "recipe_id";
    // TODO: Customize parameters
    private int recipeId;
    private RecipeDatabase db;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static IngredientFragment newInstance(int recipeId) {
        IngredientFragment fragment = new IngredientFragment();

        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, recipeId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipeId = getArguments().getInt(RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<Ingredient> ingredientList = db.getIngredientListByRecipeId(recipeId);

            recyclerView.setAdapter(new MyIngredientRecyclerViewAdapter(ingredientList));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        db = new RecipeDatabase(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        db.close();
    }
}
