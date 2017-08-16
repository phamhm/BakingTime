package com.example.hp.bakingapp.InstructionActivity.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.bakingapp.InstructionActivity.IngredientFragment;
import com.example.hp.bakingapp.InstructionActivity.InstructionDetailActivity;
import com.example.hp.bakingapp.InstructionActivity.InstructionDetailFragment;
import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;
import com.example.hp.bakingapp.Utilities.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by hp on 8/12/17.
 */

public class InstructionListAdapter
        extends RecyclerView.Adapter<InstructionListViewHolder> {
    final public static int POSITION_INGREDIENTS = 9999;
    final public static int POSITION_INSTRUCTIONS = 8888;

    private int recipeId;
    private RecipeDatabase db;
    private List<Step> instructionList;
    private FragmentManager fm;
    private boolean mTwoPane;

    public InstructionListAdapter(FragmentManager fm, RecipeDatabase db,
                                  int recipeId, boolean mTwoPane) {
        this.recipeId = recipeId;
        instructionList = db.getStepListByRecipeId(recipeId);
        this.fm = fm;
        this.mTwoPane = mTwoPane;
    }

    public void updateData(RecipeDatabase db, int recipeId){
        this.recipeId = recipeId;
        this.notifyDataSetChanged();
        this.db = db;
        instructionList = db.getStepListByRecipeId(recipeId);
    }

    @Override
    public InstructionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.instruction_list_content,parent, false);

        return new InstructionListViewHolder(view, recipeId, mTwoPane);
    }

    @Override
    public void onBindViewHolder(InstructionListViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == POSITION_INGREDIENTS)
            holder.bind("Ingredients", itemViewType, -1, this.fm, position);
        else{
            int instructionIndex = position - 1;
            Step instruction = instructionList.get(instructionIndex);
            String shortDesc = instruction.getShortDescription();
            int instructionId = instruction.getId();

            holder.bind(shortDesc, itemViewType, instructionId, this.fm, position);
        }
    }

    @Override
    public int getItemCount() {
        // include 1 item for the ingredient
        return instructionList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return POSITION_INGREDIENTS;
        else
            return POSITION_INSTRUCTIONS;
    }
}

class InstructionListViewHolder extends RecyclerView.ViewHolder {
    private int recipeId;
    private int instructionId;
    private int itemViewType;
    private boolean mTwoPane;

    @BindView(R.id.content)
    TextView instruction;

    public InstructionListViewHolder(View itemView, int recipeId, boolean mTwoPane) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.recipeId = recipeId;
        this.mTwoPane = mTwoPane;
    }

    public void bind(String text, final int itemViewType, int instructionId,
                     final FragmentManager fm,
                     final int position){
        this.itemViewType = itemViewType;
        this.instructionId = instructionId;

        instruction.setText(text);
        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Fragment fragment;
                    if (itemViewType == InstructionListAdapter.POSITION_INGREDIENTS){
                        fragment = IngredientFragment.newInstance(recipeId);
                    } else {
                        fragment = InstructionDetailFragment.newInstance(recipeId,
                                position-1);
                    }

                    fm.beginTransaction()
                            .replace(R.id.instruction_detail_container, fragment)
                            .commit();
                } else {
                    Context context = instruction.getContext();

                    Intent intent = InstructionDetailActivity.newInstance(context,
                            recipeId, itemViewType, position);

                    context.startActivity(intent);
                }
            }
        });
    }
}