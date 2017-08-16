package com.example.hp.bakingapp.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 8/10/17.
 */

public class RecipeDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Recipe.db";
    public static int DATABASE_VERSION = 3;

    //recipe table
    public static final String RECIPE_TABLE_NAME = "recipes";
    public static final String RECIPE_ID_COL = "recipe_id";
    public static final String RECIPE_NAME_COL = "name";

    public static final String INGREDIENT_TABLE_NAME = "ingredients";
    public static final String INGREDIENT_ID_COL = "ingredient_id";
    public static final String INGREDIENT_QUANTITY_COL = "quantity";
    public static final String INGREDIENT_MEASURE_COL = "measure";
    public static final String INGREDIENT_INGREDIENT_COL = "ingredient";
    public static final String INGREDIENT_RECIPE_ID_COL = RECIPE_ID_COL;


    public static final String STEP_TABLE_NAME = "steps";
    public static final String STEP_ID_COL = "step_id";
    public static final String STEP_JSON_ID_COL = "step_json_id";
    public static final String STEP_SHORT_DESC_COL = "short_desc";
    public static final String STEP_DESCRIPTION_COL = "description";
    public static final String STEP_VIDEO_URL_COL = "video_url";
    public static final String STEP_THUMBNAIL_URL_COL = "thumbnail_url";
    public static final String STEP_RECIPE_ID_COL = RECIPE_ID_COL;

    public RecipeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+RECIPE_TABLE_NAME+ " ("+
                RECIPE_ID_COL+ " integer primary key,"+
                RECIPE_NAME_COL+" text" +");");

        sqLiteDatabase.execSQL("create table "+INGREDIENT_TABLE_NAME+ " ("+
                INGREDIENT_ID_COL+ " integer primary key autoincrement,"+
                INGREDIENT_QUANTITY_COL+" real, "+
                INGREDIENT_MEASURE_COL+" text, "+
                INGREDIENT_INGREDIENT_COL+" text, "+
                INGREDIENT_RECIPE_ID_COL+" integer NOT NULL,"+
                "FOREIGN KEY ("+INGREDIENT_INGREDIENT_COL+")"+
                " REFERENCES "+RECIPE_TABLE_NAME+"("+RECIPE_ID_COL+"));");

        sqLiteDatabase.execSQL("create table "+STEP_TABLE_NAME+" ("+
                STEP_ID_COL +" integer primary key autoincrement, "+
                STEP_JSON_ID_COL+" integer not null," +
                STEP_SHORT_DESC_COL + " text, "+
                STEP_DESCRIPTION_COL+ " text, "+
                STEP_VIDEO_URL_COL+" text,"+
                STEP_THUMBNAIL_URL_COL+" text, "+
                STEP_RECIPE_ID_COL+" integer NOT NULL,"+

                "FOREIGN KEY ("+STEP_RECIPE_ID_COL+") REFERENCES "+
                RECIPE_TABLE_NAME+"("+RECIPE_ID_COL+"));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+RECIPE_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists "+INGREDIENT_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists "+STEP_TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    public void cleanupData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+RECIPE_TABLE_NAME);
        db.execSQL("delete from "+INGREDIENT_TABLE_NAME);
        db.execSQL("delete from "+STEP_TABLE_NAME);
    }

    public ArrayList<Recipe> getRecipeList(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RECIPE_TABLE_NAME, null,
                null, null, null, null, RECIPE_ID_COL);

        ArrayList<Recipe> recipeList = new ArrayList<>();
        int recipeIdIndex = cursor.getColumnIndex(RECIPE_ID_COL);

        while(cursor.moveToNext()){
            int recipeId = cursor.getInt(recipeIdIndex);
            Recipe tmpRecipe = getRecipeById(recipeId);
            recipeList.add(tmpRecipe);
        }
        cursor.close();        db.close();

        return recipeList;
    }

    public int getRecipeCount(){
        List<Recipe> recipeList = getRecipeList();
        if (recipeList == null)
            return 0;

        return recipeList.size();
    }

    public int getIngredientCountByRecipeId(int recipeId){
        List<Ingredient> ingredientList = getIngredientListByRecipeId(recipeId);
        if (ingredientList == null )
            return 0;

        return ingredientList.size();
    }

    public int getStepCountByRecipeId(int recipeId){
        ArrayList<Step> stepList = getStepListByRecipeId(recipeId);
        if(stepList == null)
            return 0;

        return stepList.size();
    }

    public String getRecipeNameById (int recipe_id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RECIPE_TABLE_NAME,
                new String[]{RECIPE_NAME_COL},
                RECIPE_ID_COL+"=?",
                new String[]{""+recipe_id},
                null, null, null, null);
        if ( cursor == null || cursor.getCount() != 1)
            return null;
        int nameIndex = cursor.getColumnIndex(RECIPE_NAME_COL);

        cursor.moveToFirst();
        String recipeName = cursor.getString(nameIndex);
        cursor.close();        db.close();

        return recipeName;
    }

    public Recipe getRecipeById(int recipe_id){

        String recipeName = getRecipeNameById(recipe_id);

        List<Ingredient> ingredients = getIngredientListByRecipeId(recipe_id);
        List<Step> steps = getStepListByRecipeId(recipe_id);

        return new Recipe(recipe_id, recipeName, ingredients, steps);
    }

    public ArrayList<Ingredient> getIngredientListByRecipeId(int recipe_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(INGREDIENT_TABLE_NAME,
                null,
                INGREDIENT_RECIPE_ID_COL+"=?",
                new String[]{""+recipe_id},
                null,null,INGREDIENT_ID_COL,null);

        if ( cursor == null || cursor.getCount() == 0)
            return null;

        ArrayList<Ingredient> ingredientList = new ArrayList<>();

        int quantityIndex = cursor.getColumnIndex(INGREDIENT_QUANTITY_COL);
        int measureIndex = cursor.getColumnIndex(INGREDIENT_MEASURE_COL);
        int ingredientIndex = cursor.getColumnIndex(INGREDIENT_INGREDIENT_COL);

        while(cursor.moveToNext()){
            float quantity = cursor.getFloat(quantityIndex);
            String measure = cursor.getString(measureIndex);
            String ingredient = cursor.getString(ingredientIndex);

            ingredientList.add(new Ingredient(quantity, measure, ingredient));
        }
        cursor.close();db.close();
        return ingredientList;
    }

    public Step getStepByRecipeId(int recipe_id, int step_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STEP_TABLE_NAME,
                null,
                STEP_RECIPE_ID_COL+"=? AND "+STEP_JSON_ID_COL+"=?",
                new String[]{""+recipe_id, ""+step_id},
                null,null,null,null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        int idIndex = cursor.getColumnIndex(STEP_JSON_ID_COL);
        int shortDescIndex = cursor.getColumnIndex(STEP_SHORT_DESC_COL);
        int descIndex = cursor.getColumnIndex(STEP_DESCRIPTION_COL);
        int videoUrlIndex = cursor.getColumnIndex(STEP_VIDEO_URL_COL);
        int thumbnailIndex = cursor.getColumnIndex(STEP_THUMBNAIL_URL_COL);

        cursor.moveToFirst();
        Log.d("cursor info", cursor.getCount()+" "+recipe_id+"~"+step_id);
        int id = cursor.getInt(idIndex);
        String shortDesc = cursor.getString(shortDescIndex);
        String description = cursor.getString(descIndex);
        String videoUrl = cursor.getString(videoUrlIndex);
        String thumbnail = cursor.getString(thumbnailIndex);
        cursor.close();        db.close();

        return new Step(id, shortDesc, description, videoUrl, thumbnail);
    }

    public ArrayList<Step> getStepListByRecipeId(int recipe_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STEP_TABLE_NAME,
                null,
                STEP_RECIPE_ID_COL+"=?",
                new String[]{""+recipe_id},
                null,null,STEP_ID_COL,null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        ArrayList<Step>  stepList = new ArrayList<>();
        int idIndex = cursor.getColumnIndex(STEP_JSON_ID_COL);
        int shortDescIndex = cursor.getColumnIndex(STEP_SHORT_DESC_COL);
        int descIndex = cursor.getColumnIndex(STEP_DESCRIPTION_COL);
        int videoUrlIndex = cursor.getColumnIndex(STEP_VIDEO_URL_COL);
        int thumbnailIndex = cursor.getColumnIndex(STEP_THUMBNAIL_URL_COL);
        while(cursor.moveToNext()){
            int id = cursor.getInt(idIndex);
            String shortDesc = cursor.getString(shortDescIndex);
            String description = cursor.getString(descIndex);
            String videoUrl = cursor.getString(videoUrlIndex);
            String thumbnail = cursor.getString(thumbnailIndex);
            stepList.add(new Step(id, shortDesc, description, videoUrl, thumbnail));
        }

        cursor.close();        db.close();

        return stepList;
    }

    public void insertRecipeFromList(List<Recipe> recipes){
        for(Recipe recipe: recipes)
            insertRecipe(recipe);
    }

    public void insertRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();

        final int recipeId = recipe.getId();

        ContentValues contentValues = new ContentValues();

        contentValues.put(RECIPE_ID_COL, recipeId);
        contentValues.put(RECIPE_NAME_COL, recipe.getName());
        db.insert(RECIPE_TABLE_NAME, null, contentValues);

        for(Ingredient ingredient : recipe.getIngredients()){
            contentValues.clear();
            contentValues.put(INGREDIENT_MEASURE_COL, ingredient.getMeasure());
            contentValues.put(INGREDIENT_QUANTITY_COL, ingredient.getQuantity());
            contentValues.put(INGREDIENT_INGREDIENT_COL, ingredient.getIngredient());
            contentValues.put(INGREDIENT_RECIPE_ID_COL, recipeId);
            db.insert(INGREDIENT_TABLE_NAME, null, contentValues);
        }

        for (Step step: recipe.getSteps()){
            contentValues.clear();
            contentValues.put(STEP_JSON_ID_COL, step.getId());
            contentValues.put(STEP_DESCRIPTION_COL, step.getDescription());
            contentValues.put(STEP_SHORT_DESC_COL, step.getShortDescription());
            contentValues.put(STEP_THUMBNAIL_URL_COL, step.getThumbnailURL());
            contentValues.put(STEP_VIDEO_URL_COL, step.getVideoURL());
            contentValues.put(STEP_RECIPE_ID_COL, recipeId);
            db.insert(STEP_TABLE_NAME, null, contentValues);
        }
    }
}
