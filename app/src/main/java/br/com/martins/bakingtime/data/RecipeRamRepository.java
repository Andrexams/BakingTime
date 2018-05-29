package br.com.martins.bakingtime.data;

import java.util.ArrayList;
import java.util.List;

import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.model.Ingredient;
import br.com.martins.bakingtime.model.Recipe;
import br.com.martins.bakingtime.model.Step;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class RecipeRamRepository implements Repository {

    private final static List<Recipe> mRecipes = new ArrayList<Recipe>();

    @Override
    public  void persistList(List<Recipe> list) {
        mRecipes.clear();
        for(Recipe recipe : list){
            mRecipes.add(recipe);
        }
    }

    @Override
    public List<Recipe> getListRecipes() {
        return mRecipes;
    }

    @Override
    public List<Ingredient> getListIngredient(Long recipeId) {
        for(Recipe recipe : mRecipes){
            if(recipe.getId().equals(recipeId)){
                return recipe.getIngredients();
            }
        }
        return new ArrayList<Ingredient>();
    }

    @Override
    public List<Step> getListStep(Long recipeId) {
        for(Recipe recipe : mRecipes){
            if(recipe.getId().equals(recipeId)){
                return recipe.getSteps();
            }
        }
        return new ArrayList<Step>();
    }
}
