import org.junit.*;
import org.openqa.selenium.By;

import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class IntegrationTest {

    @Test
    public void addVolunteerTest() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/volunteers/new");

            // add a volunteer
            browser.fill("input[name='firstName']").with("first");
            browser.fill("input[name='lastName']").with("last");
            browser.fill("input[name='address1']").with("123 Main Street");
            browser.fill("input[name='city']").with("Testville");
            browser.fill("input[name='state']").with("MN");
            browser.fill("input[name='zipCode']").with("55116");
            browser.submit("button[type='submit']");

            // make sure the table is not empty
            assertFalse(browser.$("table td a").isEmpty());

            // go to the volunteer detail page
            browser.$("table td a").first().click();

            // assert "test volunteer" is present
            assertTrue(browser.$("legend").getText().contains("first last"));

        });
    }

    @Test
    public void deleteVolunteerTest() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/volunteers/new");

            // add a volunteer
            browser.fill("input[name='firstName']").with("first");
            browser.fill("input[name='lastName']").with("last");
            browser.fill("input[name='address1']").with("123 Main Street");
            browser.fill("input[name='city']").with("Testville");
            browser.fill("input[name='state']").with("MN");
            browser.fill("input[name='zipCode']").with("55116");
            browser.submit("button[type='submit']");

            // make sure the table is not empty
            assertFalse(browser.$("table td a").isEmpty());

            // delete the volunteer by clicking the trash button
            browser.find(".glyphicon-trash").click();

            // make sure the "no volunteers" help block is present
            assertTrue(browser.find((".help-block")).getText().contains("There are no volunteers in the database."));

        });


    }

    @Test
    public void addLocationTest() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/locations/new");

            // add a location
            browser.fill("input[name='name']").with("test location");
            browser.fill("input[name='address1']").with("123 Test Street");
            browser.fill("input[name='city']").with("Testville");
            browser.fill("input[name='state']").with("MN");
            browser.fill("input[name='zipCode']").with("55116");
            browser.submit("button[type='submit']");

            // make sure the table is not empty
            assertFalse(browser.$("table td a").isEmpty());

            // go to the location detail page
            browser.$("table td a").first().click();

            // assert "test location" is present
            assertTrue(browser.$("legend").getText().contains("test location"));

        });
    }

    @Test
    public void deleteLocationTest() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/locations/new");

            // add a location
            browser.fill("input[name='name']").with("test location");
            browser.fill("input[name='address1']").with("123 Test Street");
            browser.fill("input[name='city']").with("Testville");
            browser.fill("input[name='state']").with("MN");
            browser.fill("input[name='zipCode']").with("55116");
            browser.submit("button[type='submit']");

            // make sure the table is not empty
            assertFalse(browser.$("table td a").isEmpty());

            // delete the location by clicking the trash button
            browser.find(".glyphicon-trash").click();

            // make sure the "no locations" help block is present
            assertTrue(browser.find((".help-block")).getText().contains("There must be at least one location"));


        });

    }

    @Test
    public void addAssignmentToLocationTest() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/locations/new");

            // add a location
            browser.fill("input[name='name']").with("test location");
            browser.fill("input[name='address1']").with("123 Test Street");
            browser.fill("input[name='city']").with("Testville");
            browser.fill("input[name='state']").with("MN");
            browser.fill("input[name='zipCode']").with("55116");
            browser.submit("button[type='submit']");

            // make sure the location table is not empty
            assertFalse(browser.$("table td a").isEmpty());

            // go to the location detail page
            browser.$("table td a").first().click();
            assertTrue(browser.$("legend").getText().contains("test location"));

            // make sure the assignment table is empty
            assertTrue(browser.$("table td a").isEmpty());

            // click the new assignment link
            browser.find(".add-link").click();
            assertTrue(browser.$("legend").getText().contains("Assignment (New)"));

            // add a new assignment
            browser.fill("input[name='assignmentName']").with("test assignment");
            browser.submit("button[type='submit']");

            // go to the assignment detail page
            browser.find(".glyphicon-pencil").click();

            // assert "test assignment" is present
            assertTrue(browser.$("legend").getText().contains("test assignment"));

        });
    }

    @Test
    public void removeAssignmentFromLocationTest() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/locations/new");

            // add a location
            browser.fill("input[name='name']").with("test location");
            browser.fill("input[name='address1']").with("123 Test Street");
            browser.fill("input[name='city']").with("Testville");
            browser.fill("input[name='state']").with("MN");
            browser.fill("input[name='zipCode']").with("55116");
            browser.submit("button[type='submit']");

            // make sure the location table is not empty
            assertFalse(browser.$("table td a").isEmpty());

            // go to the location detail page
            browser.$("table td a").first().click();
            assertTrue(browser.$("legend").getText().contains("test location"));

            // make sure the assignment table is empty
            assertTrue(browser.$("table td a").isEmpty());

            // click the new assignment link
            browser.find(".add-link").click();
            assertTrue(browser.$("legend").getText().contains("Assignment (New)"));

            // add a new assignment
            browser.fill("input[name='assignmentName']").with("test assignment");
            browser.submit("button[type='submit']");

            // make sure the assignment is in the assignment table
            assertTrue(browser.$("table td").first().getText().contains("test assignment"));

            // delete the assignment
            browser.find(".glyphicon-trash").click();

            // make sure the "no assignments" text is visible
            assertTrue(browser.find(("p")).getText().contains("There are no assignments for this location"));



        });


    }
}
