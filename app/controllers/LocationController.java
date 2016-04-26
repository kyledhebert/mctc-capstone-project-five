package controllers;

import com.google.inject.Inject;
import models.Location;
import org.hibernate.Criteria;
import org.hibernate.Session;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.locations.locations;
import views.html.locations.details;
import views.html.locations.newlocation;

import javax.persistence.EntityManager;
import java.util.List;

public class LocationController  extends Controller {

    private FormFactory formFactory;
    private JPAApi jpaApi;

    // inject the JPA API and a form factory so they can be used globally
    @Inject
    public LocationController(JPAApi jpaApi, FormFactory formFactory) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    // returns a Hibernate session for database transactions
    private Session getSession() {
        EntityManager entityManager  = jpaApi.em();
        return (Session) entityManager.getDelegate();
    }

    // builder for creating Location objects
    // helps with non-required fields
    private Location buildLocation(Location location) {
        return new Location.LocationBuilder(location.getName(), location.getAddress1(),
                location.getCity(), location.getState(), location.getZipCode())
                .withAddress2(location.getAddress2())
                .build();
    }

    // creates a form from the Location class parameters
    private Form<Location> createLocationForm() {
        return formFactory.form(Location.class);
    }


    // returns a list of all the locations in the database
    @Transactional
    @SuppressWarnings("unchecked")
    public Result list() {

        Session session = getSession();

        Criteria criteria = session.createCriteria(Location.class);

        List<Location> locationList = criteria.list();

         return ok(locations.render(locationList));

    }

    // renders a form for capturing a new location
    public Result newLocation() {
        Form<Location> locationForm = createLocationForm();

        return ok(newlocation.render(locationForm));
    }

    // render a form for displaying and editing an assignment's details
    @Transactional
    public Result details(int id) {
        final Location location = getLocationById(id);
        if (location == null) {
            return notFound(String.format("Location %d does not exist", id));
        }

        // creates the form and pre-fills with location details
        Form<Location> locationForm = createLocationForm();
        Form<Location> filledForm = locationForm.fill(location);

        return ok(details.render(location, filledForm));
    }

    private Location getLocationById(int id) {
        Session session = getSession();

        // gets the location from the database or null if not found
        return session.get(Location.class, id);

    }

    // saves a new location to the database using info from the form
    @Transactional
    public Result save() {
        Form<Location> locationForm = createLocationForm();
        Form<Location> boundForm = locationForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below");
            return  badRequest(newlocation.render(boundForm));
        }

        // create a new location from the form entries
        Location location = boundForm.get();

        // use LocationBuilder to handle empty, non-required fields
        Location location1 = buildLocation(location);


        // save the new location with Hibernate
        Session session = getSession();
        session.save(location1);

        flash("success", String.format("Successfully added location %s", location1));

        return redirect(routes.AssignmentController.list());
    }


    // update an existing assignment
    @Transactional
    public Result update(int id) {
        Location location = getLocationById(id);
        if (location == null) {
            return notFound(String.format("Location %d does not exist", id));
        }

        // get the new data from the form
        Form<Location> locationForm = createLocationForm();
        Form<Location> boundForm = locationForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below");
            return badRequest(details.render(location, boundForm));
        }

        // update the location
        Location updatedLocation = boundForm.get();

        location.setName(updatedLocation.getName());
        location.setAddress1(updatedLocation.getAddress1());
        location.setAddress2(updatedLocation.getAddress2());
        location.setCity(updatedLocation.getCity());
        location.setState(updatedLocation.getState());
        location.setZipCode(updatedLocation.getZipCode());

        // save using Hibernate
        Session session = getSession();
        session.update(location);

        flash("success", String.format("Successfully updated location %s", location));
        return redirect(routes.AssignmentController.list());

    }

    // delete a location
    @Transactional
    public Result delete(int id) {
        final Location location = getLocationById(id);
        if (location == null) {
            return notFound(String.format("Location %d deos not exist.", id));
        }

        Session session = getSession();
        session.delete(location);

        flash("success", String.format("Successfully deleted location %s", location));
        return redirect(routes.AssignmentController.list());

    }



}
