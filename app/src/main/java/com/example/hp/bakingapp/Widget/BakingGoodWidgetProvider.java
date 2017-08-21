package com.example.hp.bakingapp.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.hp.bakingapp.InstructionActivity.InstructionListActivity;
import com.example.hp.bakingapp.R;
import com.example.hp.bakingapp.Utilities.Recipe;
import com.example.hp.bakingapp.Utilities.RecipeDatabase;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class BakingGoodWidgetProvider extends AppWidgetProvider {


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.baking_good_widget);

        RecipeDatabase db = new RecipeDatabase(context);

        int totalRecipe = db.getRecipeCount();
        if(totalRecipe != 0){

            int recipeId = new Random().nextInt(totalRecipe) + 1;
            Recipe recipe = db.getRecipeById(recipeId);
            remoteViews.setTextViewText(R.id.appwidget_text, "Recipe of the Day: "+recipe.getName());
            db.close();

            Intent intent = InstructionListActivity.newInstance(context, recipeId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

            Log.d("debug recipeId", "updateAppWidget: "+totalRecipe);

            Intent listIntent = new Intent(context, ListViewService.class);
            listIntent.putExtra(ListViewService.RECIPEID_INTENT, recipeId);
            remoteViews.setRemoteAdapter(R.id.widget_ingredient_list, listIntent);
        } else
            remoteViews.setTextViewText(R.id.appwidget_text, "Failed to Fetch Recipe");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

