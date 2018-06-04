package br.com.martins.bakingtime.data;

import java.util.ArrayList;
import java.util.List;

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
    public List<Recipe> getRecipeList() {
        return mRecipes;
    }

    public Recipe getRecipe(Long recipeId){
        List<Recipe> recipeList = getRecipeList();
        if(recipeList != null){
            for(Recipe recipe : recipeList){
                if(recipe.getId().equals(recipeId)){
                    return recipe;
                }
            }
        }
        return null;
    }


    @Override
    public List<Ingredient> getIngredientList(Long recipeId) {
        for(Recipe recipe : mRecipes){
            if(recipe.getId().equals(recipeId)){
                return recipe.getIngredients();
            }
        }
        return new ArrayList<Ingredient>();
    }

    @Override
    public List<Step> getStepList(Long recipeId) {
        for(Recipe recipe : mRecipes){
            if(recipe.getId().equals(recipeId)){
                return recipe.getSteps();
            }
        }
        return new ArrayList<Step>();
    }

    @Override
    public Step getStep(Long recipeId,Integer stepId){
        List<Step> listStep = this.getStepList(recipeId);
        if(listStep != null){
            for(Step step : listStep){
                if(step.getId().equals(stepId)){
                    return step;
                }
            }
        }
        return null;
    }

    public Integer getStepCount(Long recipeId){
        List<Step> stepList = this.getStepList(recipeId);
        if(stepList != null){
            return stepList.size();
        }
        return 0;
    }

    public String getIngredientTextList(Long recipeId){
       List<Ingredient> ingredientList = getIngredientList(recipeId);
       StringBuffer textBuff = new StringBuffer();
       if(ingredientList != null){
           for(Ingredient ingredient : ingredientList){
               textBuff.append(ingredient.toString()).append("\n");
           }
       }
       return textBuff.toString();
    }
}
