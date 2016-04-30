package models;


import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Constraints.Required
    @Constraints.MaxLength(50)
    @Column
    private String firstName;

    @Constraints.Required
    @Constraints.MaxLength(50)
    @Column
    private String lastName;

    @Constraints.Required
    @Constraints.MaxLength(50)
    @Column
    private String city;

    @Constraints.Required
    @Column
    private String status;

    @Constraints.Required
    @Constraints.MaxLength(250)
    @Column
    private String address1;

    @Constraints.MaxLength(250)
    @Column
    private String address2;

    @Constraints.Required
    @Constraints.MaxLength(2)
    @Column
    private String state;

    @Constraints.Required
    @Constraints.MaxLength(5)
    @Column
    private String zipCode;

    @Column
    private String phoneNumber;

    @Constraints.Email
    @Column
    private String email;

    @ManyToOne
    private Assignment assignment;

    // default constructor for JPA
    public Volunteer() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Volunteer(VolunteerBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.status = builder.status;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.address1 = builder.address1;
        this.address2 = builder.address2;
        this.city = builder.city;
        this.state = builder.state;
        this.zipCode = builder.zipCode;
    }

    public static class VolunteerBuilder {
        private String firstName;
        private String lastName;
        private String status;
        private String phoneNumber;
        private String email;
        private String address1;
        private String address2;
        private String city;
        private String state;
        private String zipCode;

        public VolunteerBuilder(String firstName, String lastName, String status, String address1, String city, String state, String zipCode) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.status = status;
            this.address1 = address1;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
        }

        public VolunteerBuilder withPhone(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public VolunteerBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public VolunteerBuilder withAddress2(String address2) {
            this.address2 = address2;
            return this;
        }

        public Volunteer build() {
            return new Volunteer(this);
        }

    }



    @Override
    public String toString() {
        return "Volunteer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

