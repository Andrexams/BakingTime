package br.com.martins.bakingtime.utils;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

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



}
