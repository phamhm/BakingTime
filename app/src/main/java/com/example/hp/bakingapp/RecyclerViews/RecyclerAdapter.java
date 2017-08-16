package com.example.hp.bakingapp.RecyclerViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.Recipe;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;

/**
 * Created by hp on 8/8/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private RecipeDatabase db;

    public RecyclerAdapter() {}

    public void updateData(RecipeDatabase db){
        this.db = db;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipes_list,
                        parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int recipeId = position + 1;

        if(db == null)
            return;

        String recipeName = db.getRecipeNameById(recipeId);

        if (recipeName == null || recipeName.equals(""))
            return;

        holder.bind(recipeName, recipeId);
    }

    @Override
    public int getItemCount() {
        if (db == null)
            return 0;

        return db.getRecipeCount();
    }
}
