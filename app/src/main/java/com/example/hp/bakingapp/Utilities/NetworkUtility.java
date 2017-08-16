package com.example.hp.bakingapp.Utilities;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANResponse;

import java.util.ArrayList;

/**
 * Created by hp on 8/8/17.
 */

public class NetworkUtility {
    private static String recipesUrl =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static ArrayList<Recipe> getRecipesFromServer(Context context){
        AndroidNetworking.initialize(context);
        ANResponse<ArrayList<Recipe>> response =
                AndroidNetworking.get(recipesUrl)
                        .getResponseOnlyIfCached()
                        .build()
                        .executeForObjectList(Recipe.class);

        if (response.isSuccess())
            return response.getResult();
        else
            return null;
    }
}

