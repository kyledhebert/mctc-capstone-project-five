package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Set;

/**
 * Objects of the <code>Assignment</code> class represent
 * a single Assignment in the database. <code>Assignment</code>
 * objects are JPA Entities
 * <p>
 *     <code>Assignments</code> have a Many to Many relationship with
 *     <code>Volunteers</code>, and a Many to One relationship with
 *     <code>Locations</code>.
 * </p>
 */

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

    @ManyToMany
    @JoinTable(name = "AssignmentVolunteer",
    joinColumns = {@JoinColumn(name = "assignmentId")},
    inverseJoinColumns = {@JoinColumn(name = "volunteerId")})
    private Set<Volunteer> volunteers;

    // default constructor for JPA
    public Assignment() {}


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

    public Set<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(Set<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }
}
