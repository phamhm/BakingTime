package com.example.hp.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.Ingredient;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;
import com.example.hp.bakingapp.Utilities.Step;

import java.util.List;

/**
 * Created by hp on 8/20/17.
 */

public class ListViewService extends RemoteViewsService {
    final public static String RECIPEID_INTENT = "recipe_id_intent";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Context context = this.getApplicationContext();


        return new ListViewFactory(context, intent);
    }
}

class ListViewFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context context;
    private Intent intent;
    private int recipeId;
    RecipeDatabase db;
    List<Ingredient> ingredientList;

    public ListViewFactory(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
        recipeId = intent.getIntExtra(ListViewService.RECIPEID_INTENT, -1);

        Log.d("debug view service", "service recipeId "+recipeId);

        db = new RecipeDatabase(context);
        if(recipeId != 0){
            ingredientList = db.getIngredientListByRecipeId(recipeId);
        }
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredientList == null)
            return 0;
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        Ingredient ingredient = ingredientList.get(i);

        views.setTextViewText(R.id.widget_list_item, ingredient.getIngredient());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
