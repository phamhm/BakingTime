package com.example.hp.bakingapp.InstructionActivity.RecyclerViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.Ingredient;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Ingredient} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyIngredientRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;
    public MyIngredientRecyclerViewAdapter(List<Ingredient> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Ingredient ingredient = mValues.get(position);
        holder.mIdView.setText(ingredient.getIngredient());
        holder.mContentView.setText(
                ingredient.getQuantity()+" "+
                ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Ingredient mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
