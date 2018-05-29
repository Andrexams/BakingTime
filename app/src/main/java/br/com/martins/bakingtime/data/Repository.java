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
    List<Recipe> getListRecipes();
    List<Ingredient> getListIngredient(Long recipeId);
    List<Step> getListStep(Long recipeId);
}
