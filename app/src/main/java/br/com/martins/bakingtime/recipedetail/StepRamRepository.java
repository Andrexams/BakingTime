package br.com.martins.bakingtime.recipedetail;

import java.util.ArrayList;
import java.util.List;

import br.com.martins.bakingtime.data.ByIdSpecification;
import br.com.martins.bakingtime.data.Repository;
import br.com.martins.bakingtime.data.Specification;
import br.com.martins.bakingtime.model.Ingredient;
import br.com.martins.bakingtime.model.Step;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class StepRamRepository implements Repository<Step> {

    private static List<Step> mSteps = new ArrayList<Step>();

    @Override
    public void add(Step item) {
        mSteps.add(item);
    }

    @Override
    public List<Step> query(Specification specification) {
        ByIdSpecification byIdSpecification = (ByIdSpecification)specification;
        List<Step> steps = new ArrayList<>();
        for(Step step : mSteps){
            if(step.getRecipeId().equals(byIdSpecification.getId())){
                steps.add(step);
            }
        }
        return steps;
    }
}
