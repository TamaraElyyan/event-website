package event.website.dto;

import event.website.model.Picture;
import event.website.model.Training;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public class TrainingCreationDTO {

    @NotNull(message = "Training cannot be null")
    private Training training;

    @NotEmpty(message = "Organization IDs cannot be empty")
    private Set<Integer> organizationIds;  // List of organization IDs associated with the Training

    @NotEmpty(message = "Instructor IDs cannot be empty")
    private Set<Integer> instructorIds;    // List of instructor IDs associated with the Training

    private List<Picture> pictures; // List of pictures for the Training
    // Getters and Setters
    public Training getTraining() {
        return training;
    }

    public void setEvent(Training training) {
        this.training = training;
    }

    public Set<Integer> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(Set<Integer> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public Set<Integer> getInstructorIds() {
        return instructorIds;
    }

    public void setInstructorIds(Set<Integer> instructorIds) {
        this.instructorIds = instructorIds;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
