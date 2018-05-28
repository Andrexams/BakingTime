package br.com.martins.bakingtime.model;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class Recipe {

    private Long id;
    private String name;
    private Integer servings;
    private String image;

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
