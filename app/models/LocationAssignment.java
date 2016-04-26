package models;

import javax.persistence.*;

@Entity
public class LocationAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int id;

    @ManyToOne
    public Location location;

    @ManyToOne
    public Assignment assignment;

    // default constructor for JPA
    public LocationAssignment() {}

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
