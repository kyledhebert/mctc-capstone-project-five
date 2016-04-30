package models;

import org.hibernate.annotations.*;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Constraints.Required
    @Constraints.MaxLength(50)
    @Column
    private String name;

    @Constraints.Required
    @Constraints.MaxLength(250)
    @Column
    private String address1;

    @Constraints.MaxLength(250)
    @Column
    private String address2;

    @Constraints.Required
    @Constraints.MaxLength(50)
    @Column
    private String city;

    @Constraints.Required
    @Constraints.MaxLength(2)
    @Column
    private String state;

    @Constraints.Required
    @Constraints.MinLength(5)
    @Constraints.MaxLength(5)
    @Column
    private String zipCode;

    @OneToMany(mappedBy="location", orphanRemoval = true)
    private List<Assignment> assignments;

    // default constructor for JPA
    public Location() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Location(LocationBuilder builder ) {
        this.name = builder.name;
        this.address1 = builder.address1;
        this.address2 = builder.address2;
        this.city = builder.city;
        this.state = builder.state;
        this.zipCode = builder.zipCode;
    }


    public static class LocationBuilder {
        private String name;
        private String address1;
        private String address2;
        private String city;
        private String state;
        private String zipCode;

        public LocationBuilder(String name, String address1, String city, String state,
                               String zipCode) {
            this.name = name;
            this.address1 = address1;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
        }

        public LocationBuilder withAddress2(String address2) {
            this.address2 = address2;
            return this;
        }

        public Location build() {return new Location(this);}

    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                '}';
    }
}
