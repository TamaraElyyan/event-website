package event.website.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "training")
@Where(clause = "deleted = false")


public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull(message = "Training name cannot be null")
    @Column(name = "training_name", nullable = false, unique = false)
    private String trainingName;

    @JsonIgnore
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;


    @Lob
    @Column(name = "training_description")
    private String trainingDescription;



    @Column(name = "number_of_students_enrolled")
    private Integer numberOfStudentsEnrolled;

    @Column(name = "max_number_of_students")
    private Integer maxNumberOfStudents=10;


    @Column(name = "end_registration")
    private LocalDate endRegistration;
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;



    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type", nullable = false)
    private Type type=Type.TRAINING_COURSE;



    @OneToMany(mappedBy = "training")
    private Set<TrainingInstructor> trainingInstructors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "training")
    private Set<TrainingOrganization> trainingOrganizations = new LinkedHashSet<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "training")
    private Set<event.website.model.RegistrationStudent> registrationStudents = new LinkedHashSet<>();

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    // one training have many picture
    @JsonIgnore
    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Picture> pictures = new LinkedList<>();

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getEndRegistration() {
        return endRegistration;
    }

    public void setEndRegistration(LocalDate endRegistration) {
        this.endRegistration = endRegistration;
    }



    public Integer getNumberOfStudentsEnrolled() {
        return numberOfStudentsEnrolled;
    }

    public void setNumberOfStudentsEnrolled(Integer numberOfStudentsEnrolled) {
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    public Integer getMaxNumberOfStudents() {
        return maxNumberOfStudents;
    }

    public void setMaxNumberOfStudents(Integer maxNumberOfStudents) {
        this.maxNumberOfStudents = maxNumberOfStudents;
    }


    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(String trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    public Set<TrainingInstructor> getTrainingInstructors() {
        return trainingInstructors;
    }

    public void setTrainingInstructors(Set<TrainingInstructor> trainingInstructors) {
        this.trainingInstructors = trainingInstructors;
    }

    public Set<TrainingOrganization> getTrainingOrganizations() {
        return trainingOrganizations;
    }

    public void setTrainingOrganizations(Set<TrainingOrganization> trainingOrganizations) {
        this.trainingOrganizations = trainingOrganizations;
    }

    public Set<event.website.model.RegistrationStudent> getRegistrationStudents() {
        return registrationStudents;
    }

    public void setRegistrationStudents(Set<event.website.model.RegistrationStudent> registrationStudents) {
        this.registrationStudents = registrationStudents;
    }

}