package com.example.hp.bakingapp.RecyclerViews;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.hp.bakingapp.InstructionActivity.InstructionListActivity;
import com.example.hp.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 8/8/17.
 */

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.recipe_name)
    Button recipeButton;

    private int recipeId;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (recipeButton != null){
            recipeButton.setClickable(true);
            recipeButton.setOnClickListener(this);
        }
    }

    public void bind(String recipeName, int recipeId){
        recipeButton.setText(recipeName);
        this.recipeId = recipeId;
    }

    @Override
    public void onClick(View view) {
        Intent intent =
                InstructionListActivity.newInstance(recipeButton.getContext(), recipeId);
        view.getContext().startActivity(intent);
    }
}
