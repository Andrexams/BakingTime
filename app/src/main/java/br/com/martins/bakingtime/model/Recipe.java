package br.com.martins.bakingtime.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class Recipe implements Serializable {

    private Long id;
    private String name;
    private Integer servings;
    private String image;

    private List<Ingredient> ingredients;
    private List<Step> steps;

    public Recipe(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Recipe){
            Recipe recipe = (Recipe)obj;
            if(recipe.getId().equals(this.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (this.getId() * Integer.MIN_VALUE);
    }
}
