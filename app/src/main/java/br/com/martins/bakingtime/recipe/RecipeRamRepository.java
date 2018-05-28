package br.com.martins.bakingtime.recipe;

import java.util.ArrayList;
import java.util.List;

import br.com.martins.bakingtime.data.ByIdSpecification;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.data.Specification;
import br.com.martins.bakingtime.model.Recipe;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class RecipeRamRepository implements Repository<Recipe> {

    private static List<Recipe> mRecipes = new ArrayList<Recipe>();

    @Override
    public void add(Recipe item) {
        mRecipes.add(item);
    }

    @Override
    public List<Recipe> query(Specification specification) {
        ByIdSpecification byIdSpecification = (ByIdSpecification)specification;
        List<Recipe> recipes = new ArrayList<>();
        for(Recipe recipe : mRecipes){
            if(recipe.getId().equals(byIdSpecification.getId())){
                recipes.add(recipe);
            }
        }
        return recipes;
    }
}
