package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int id;

    @Constraints.Required
    @Column
    public String assignmentName;

    @OneToMany(mappedBy = "location")
    public List<LocationAssignment> locationAssignments;


    // default constructor for JPA
    public Assignment() {}

    public Assignment(String assignmentName) {
        this.assignmentName = assignmentName;
    }

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

    public List<LocationAssignment> getLocationAssignments() {
        return locationAssignments;
    }

    public void setLocationAssignments(List<LocationAssignment> locationAssignments) {
        this.locationAssignments = locationAssignments;
    }
}
