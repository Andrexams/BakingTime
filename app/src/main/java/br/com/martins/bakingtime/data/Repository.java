package br.com.martins.bakingtime.data;

import java.util.List;

import br.com.martins.bakingtime.model.Ingredient;
import br.com.martins.bakingtime.model.Recipe;
import br.com.martins.bakingtime.model.Step;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public interface Repository {
    void persistList(List<Recipe> list);
    List<Recipe> getRecipeList();
    Recipe getRecipe(Long recipeId);
    List<Ingredient> getIngredientList(Long recipeId);
    List<Step> getStepList(Long recipeId);
    Step getStep(Long recipeId,Integer stepId);
    Integer getStepCount(Long recipeId);
    String getIngredientTextList(Long recipeId);
}
