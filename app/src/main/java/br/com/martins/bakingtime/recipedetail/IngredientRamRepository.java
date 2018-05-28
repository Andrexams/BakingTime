package br.com.martins.bakingtime.recipedetail;

import java.util.ArrayList;
import java.util.List;

import br.com.martins.bakingtime.data.ByIdSpecification;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.data.Specification;
import br.com.martins.bakingtime.model.Ingredient;
import br.com.martins.bakingtime.model.Recipe;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class IngredientRamRepository implements Repository<Ingredient> {

    private static List<Ingredient> mIngredients = new ArrayList<Ingredient>();

    @Override
    public void add(Ingredient item) {
        mIngredients.add(item);
    }

    @Override
    public List<Ingredient> query(Specification specification) {
        ByIdSpecification byIdSpecification = (ByIdSpecification)specification;
        List<Ingredient> ingredients = new ArrayList<>();
        for(Ingredient ingredient : mIngredients){
            if(ingredient.getRecipeId().equals(byIdSpecification.getId())){
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }
}
