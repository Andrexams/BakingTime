package br.com.martins.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import br.com.martins.bakingtime.data.RecipeRamRepository;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingTimeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Long recipeId) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_widget);

            if(recipeId != null){
                String ingredients = getIngredients(recipeId);
                String recipeName = getRecipeName(recipeId);
                views.setTextViewText(R.id.appwidget_recipe_name, recipeName);
                views.setTextViewText(R.id.appwidget_ingredients, ingredients);
            }

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.appwidget_recipe_name,pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
    }

    public static void updateIngredientsWidget(Context context,
                                               AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds,
                                               Long recipeId){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context,appWidgetManager,appWidgetId,recipeId);
        }
    }

    public static String getIngredients(Long recipeId){
        Repository repository = new RecipeRamRepository();
        String ingredientTextList = repository.getIngredientTextList(recipeId);
        if(ingredientTextList.length() > 0){
            return ingredientTextList;
        }
        return null;
    }

    public static String getRecipeName(Long recipeId){
        Repository repository = new RecipeRamRepository();
        Recipe recipe = repository.getRecipe(recipeId);
        if(recipe != null){
            return recipe.getName();
        }
        return null;
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

