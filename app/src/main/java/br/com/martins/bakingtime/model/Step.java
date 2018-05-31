package br.com.martins.bakingtime.model;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class Step {

    private Integer id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(Long recipeId) {
        this.id = id;
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
            if (step.getId().equals(this.getId())) {
                return true;
            }
        }
        return false;
    }
}
