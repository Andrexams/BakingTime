package br.com.martins.bakingtime.model;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class Step {

    private Long recipeId;
    private Integer id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(Long recipeId, Integer id) {
        this.recipeId = recipeId;
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Step) {
            Step step = (Step) obj;
            if (step.getId().equals(this.getId())
                    && step.getRecipeId().equals(this.getRecipeId())) {
                return true;
            }
        }
        return false;
    }
}
