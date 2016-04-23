package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.HashSet;
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

    @ManyToOne
    @JoinColumn
    Location location;


    // default constructor for JPA
    public Assignment() {}

    public Assignment(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "activityName='" + assignmentName + '\'' +
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

}
