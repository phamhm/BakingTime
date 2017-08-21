package com.example.hp.bakingapp.Utilities;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.hp.bakingapp.RecyclerViews.RecyclerAdapter;

import java.util.List;

public class NetworkUtility {
    private static String recipesUrl =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static void getRecipesFromServer(final RecyclerAdapter mAdapter,
                                            final RecipeDatabase db){
        AndroidNetworking.get(recipesUrl)
                .build()
                .getAsObjectList(Recipe.class, new ParsedRequestListener<List<Recipe>>() {
                    @Override
                    public void onResponse(List<Recipe> response) {
                        db.insertRecipeFromList(response);
                        if (mAdapter != null)
                             mAdapter.updateData(db);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Networking error", anError.toString());
                    }
                });
    }
}

