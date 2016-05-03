import org.junit.*;

import play.mvc.*;
import play.test.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


import static org.fluentlenium.core.filter.FilterConstructor.*;

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

}
