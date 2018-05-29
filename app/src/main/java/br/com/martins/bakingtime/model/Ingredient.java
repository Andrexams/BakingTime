package br.com.martins.bakingtime.model;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class Ingredient {

    private Long id;
    private Long recipeId;
    private Double quantity;
    private String measure;
    private String name;

    public Ingredient(Long id, Long recipeId) {
        this.id = id;
        this.recipeId = recipeId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ingredient){
            Ingredient ingredient = (Ingredient)obj;
            if(ingredient.getId().equals(this.getId()) &&
                    ingredient.getRecipeId().equals(this.getRecipeId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int)(id + recipeId * Integer.MAX_VALUE);
    }
}
