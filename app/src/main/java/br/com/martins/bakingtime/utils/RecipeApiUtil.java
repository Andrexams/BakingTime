package br.com.martins.bakingtime.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.martins.bakingtime.model.Ingredient;
import br.com.martins.bakingtime.model.Recipe;
import br.com.martins.bakingtime.model.Step;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class RecipeApiUtil {

    private static String TAG = RecipeApiUtil.class.getSimpleName();

    public static String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static URL buildRecipesUrl() {
        Uri builtUri = Uri.parse(RECIPES_URL).buildUpon()
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Built URI " + url);
        return url;
    }

    public static List<Recipe> getRecipeList() throws Exception{
        String recipesJson = NetworkUtils.getJSonResponseFromHttpUrl(RecipeApiUtil.buildRecipesUrl());
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Recipe>>(){}.getType();
        List<Recipe> list =  gson.fromJson(recipesJson, collectionType);
        return list;
    }

    public List<Recipe> getRecipeList_(String jsonRecipes) throws Exception{

        List<Recipe> recipes = new ArrayList<>();

        JSONObject jsonRecipesObj = new JSONObject(jsonRecipes);
        JSONArray resultsArray = jsonRecipesObj.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject recipeJsonObj = resultsArray.getJSONObject(i);

            Recipe recipe = new Recipe(recipeJsonObj.getLong("id"));
            recipe.setName(recipeJsonObj.optString("name"));
            recipe.setImage(recipeJsonObj.optString("image"));
            recipe.setServings(recipeJsonObj.optInt("servings"));

            JSONArray ingredientsJsonObj = recipeJsonObj.getJSONArray("ingredients");
            List<Ingredient> ingredientList = getIngredients(ingredientsJsonObj);

            JSONArray stepsJsonObj = recipeJsonObj.getJSONArray("steps");
            List<Step> stepsList = getSteps(stepsJsonObj,recipe.getId());

            recipes.add(recipe);
        }

        return recipes;
    }

    private List<Step> getSteps(JSONArray stepsJsonObj, Long recipeId) throws Exception {
        List<Step> listSteps = new ArrayList<>();
        for (int i = 0; i < stepsJsonObj.length(); i++) {
            JSONObject jsonStepObject = stepsJsonObj.getJSONObject(i);
            Step step = new Step(recipeId,jsonStepObject.getInt("id"));
            step.setDescription(jsonStepObject.optString("description"));
            step.setShortDescription(jsonStepObject.optString("shortDescription"));
            step.setThumbnailURL(jsonStepObject.optString("thumbnailURL"));
            step.setVideoURL(jsonStepObject.optString("videoURL"));
            listSteps.add(step);
        }
        return listSteps;
    }

    private List<Ingredient> getIngredients(JSONArray ingredientsJsonObj) throws Exception {
        List<Ingredient> listIngredient = new ArrayList<>();
        for (int i = 0; i < ingredientsJsonObj.length(); i++) {
            JSONObject jsonIngredientObject = ingredientsJsonObj.getJSONObject(i);

        }
        return listIngredient;
    }

}
