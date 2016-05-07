import models.Assignment;
import models.Location;
import models.Volunteer;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void createVolunteerTest() {

        Volunteer.VolunteerBuilder volunteerBuilder = new Volunteer.VolunteerBuilder("first",
                "last", "Active", "123 Test Street", "Testville", "MN", "55116");

        Volunteer volunteer = new Volunteer(volunteerBuilder);

        assertTrue(volunteer.getFirstName().equals("first"));
        assertTrue(volunteer.getLastName().equals("last"));
        assertTrue(volunteer.getStatus().equals("Active"));
        assertTrue(volunteer.getAddress1().equals("123 Test Street"));
        assertTrue(volunteer.getCity().equals("Testville"));
        assertTrue(volunteer.getState().equals("MN"));
        assertTrue(volunteer.getZipCode().equals("55116"));
    }

    @Test
    public void createLocationTest() {

        Location.LocationBuilder locationBuilder = new Location.LocationBuilder("test location",
                "123 test street", "testville", "MN", "55116");

        Location location = new Location(locationBuilder);

        assertTrue(location.getName().equals("test location"));
        assertTrue(location.getAddress1().equals("123 test street"));
        assertTrue(location.getCity().equals("testville"));
        assertTrue(location.getState().equals("MN"));
        assertTrue(location.getZipCode().equals("55116"));
    }

    @Test
    public void createAssignmentTest() {
        Assignment assignment = new Assignment();

        assignment.setAssignmentName("test assignment");

        assertTrue(assignment.getAssignmentName().equals("test assignment"));
    }

}
