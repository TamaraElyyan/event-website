package event.website.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;

import java.time.Instant;

@Entity
@Table(name = "picture")
@Where(clause = "deleted = false")

public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "listing_id", nullable = false)
    private Integer listing;

    @NotNull
    @Column(name = "url", nullable = false, length = 250)
    private String url;

    @Column(name = "is_user", nullable = false)
    private boolean isUser = true;

    public boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(boolean user) {
        isUser = user;
    }

    @NotNull
    @Column(name = "upload_date", nullable = false)
    private Instant uploadDate;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @JsonIgnore
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getListing() {
        return listing;
    }

    public void setListing(Integer listing) {
        this.listing = listing;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

}