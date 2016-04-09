package models;


import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Constraints.Required
    @Column
    public String firstName;

    @Constraints.Required
    @Column
    public String lastName;

    //@Constraints.Email
    @Constraints.Required
    @Column
    public String email;

    // default constructor for JPA
    public Volunteer() {}

    public Volunteer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

