package models;

import java.util.ArrayList;
import java.util.List;

public class Volunteer {

    public String idn;
    public String name;

    public Volunteer() {}

    public Volunteer(String idn, String name) {
        this.idn = idn;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", idn, name);
    }

    public static List<Volunteer> getVolunteerList() {
        return new ArrayList<Volunteer>(volunteerList);
    }

    public static Volunteer getVolunteerByIdn(String idn) {
        for (Volunteer volunteer : volunteerList) {
            if (volunteer.idn.equals(idn)) {
                return volunteer;
            }
        }
        return null;
    }

    public static List<Volunteer> getVolunteerListByName(String searchTerm) {
        final List<Volunteer> results = new ArrayList<Volunteer>();
        for (Volunteer volunteer : results) {
            if (volunteer.name.toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(volunteer);
            }
        }

        return results;
    }

    public static boolean removeVolunteer(Volunteer volunteer) {
        return volunteerList.remove(volunteer);
    }

    public void save() {
        volunteerList.remove(getVolunteerByIdn(this.idn));
        volunteerList.add(this);
    }


    // Sample Data
    private static List<Volunteer> volunteerList;

    static {
        volunteerList = new ArrayList<Volunteer>();
        volunteerList.add(new Volunteer("111111", "Volunteer 1"));
        volunteerList.add(new Volunteer("222222", "Volunteer 2"));
        volunteerList.add(new Volunteer("333333", "Volunteer 3"));
        volunteerList.add(new Volunteer("444444", "Volunteer 4"));
    }

}

