package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by kylehebert on 5/1/16.
 */
@Entity
public class AssignmentVolunteer implements Serializable {


    @Id
    @Column
    private int assignmentId;

    @Id
    @Column
    private int volunteerId;

    public AssignmentVolunteer() {}

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(int volunteerId) {
        this.volunteerId = volunteerId;
    }
}
