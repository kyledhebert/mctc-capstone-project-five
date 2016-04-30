package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import javax.persistence.Entity;
import java.awt.*;
import java.util.List;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Constraints.Required
    @Column
    private String assignmentName;

    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "assignment", orphanRemoval = true)
    private List<Volunteer> volunteers;

    // default constructor for JPA
    public Assignment() {}

//    public Assignment(String assignmentName) {
//        this.assignmentName = assignmentName;
//    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentName='" + assignmentName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String activityName) {
        this.assignmentName = activityName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }
}
