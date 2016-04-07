package models;


import play.data.validation.Constraints;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Volunteer {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
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



    public static List<Volunteer> getVolunteerList() {
        return new ArrayList<Volunteer>(volunteerList);
    }

    public static Volunteer getVolunteerById(int id) {
        for (Volunteer volunteer : volunteerList) {
            if (volunteer.id == id) {
                return volunteer;
            }
        }
        return null;
    }

    public static List<Volunteer> getVolunteerListByName(String searchTerm) {
        final List<Volunteer> results = new ArrayList<Volunteer>();
        for (Volunteer volunteer : results) {
            if (volunteer.firstName.toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(volunteer);
            }
        }

        return results;
    }

    public static boolean removeVolunteer(Volunteer volunteer) {
        return volunteerList.remove(volunteer);
    }

    public void save() {
        volunteerList.remove(getVolunteerById(this.id));
        volunteerList.add(this);
    }


    // Sample Data
    private static List<Volunteer> volunteerList;

    static {
        volunteerList = new ArrayList<Volunteer>();
        volunteerList.add(new Volunteer("Sam", "Barlow", "sam@barlow.com"));
        volunteerList.add(new Volunteer("Liz", "England", "liz@england.com"));
        volunteerList.add(new Volunteer("Rami", "Ismael", "rami@vlambeer.com"));
    }

}

