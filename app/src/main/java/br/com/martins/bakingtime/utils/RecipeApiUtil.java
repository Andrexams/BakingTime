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
}
