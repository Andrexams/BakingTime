package br.com.martins.bakingtime.model;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class Ingredient {

    private Double quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {

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

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ingredient){
            Ingredient ingredient = (Ingredient)obj;
            if(ingredient.getIngredient().equals(this.getIngredient())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return quantity + " " + measure + " of " + ingredient;
    }
}
