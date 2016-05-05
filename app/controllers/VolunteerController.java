package controllers;


import com.google.inject.Inject;
import models.Assignment;
import models.Volunteer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.volunteers.details;
import views.html.volunteers.newvolunteer;
import views.html.volunteers.volunteers;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;


/**
 * This controller manages persistence operations for <code>Volunteers</code>.
 *
 * This controller uses JPA and Hibernate to create, read, update
 * and delete <code>Volunteers</code> using database transactions.
 *
 * @author Kyle D. Hebert
 * @version 0.2
 *
 */

public class VolunteerController extends Controller {

    private FormFactory formFactory;
    private JPAApi jpaApi;

    // inject the JPA API and a form factory so they can be used globally
    @Inject
    public VolunteerController(JPAApi jpaApi, FormFactory formFactory) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    // returns a Hibernate session for database transactions
    private Session getSession() {
        EntityManager entityManager  = jpaApi.em();
        return (Session) entityManager.getDelegate();
    }

    // builder for creating Volunteer objects
    // helps with non-required fields
    private Volunteer buildVolunteer(Volunteer volunteer) {
        return new Volunteer.VolunteerBuilder(volunteer.getFirstName(),
                volunteer.getLastName(), volunteer.getStatus(), volunteer.getAddress1(),
                volunteer.getCity(), volunteer.getState(), volunteer.getZipCode())
                .withPhone(volunteer.getPhoneNumber())
                .withEmail(volunteer.getEmail())
                .withAddress2(volunteer.getAddress2())
                .build();
    }


    /**
     * An action that renders an HTML view that lists all
     * volunteers in the database.
     *
     * @return A result for rendering volunteers.html with a list of
     * volunteers.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public Result list() {

        Session session = getSession();

        Criteria criteria = session.createCriteria(Volunteer.class);

        List<Volunteer> volunteerList = criteria.list();

        // also need to pass the dynamic form for rendering
        DynamicForm searchForm = formFactory.form();

        return ok(volunteers.render(volunteerList, searchForm));
    }

    /**
     * An Action that lists all of the volunteers whose name last
     * name begins with the provided letter.
     *
     * @param letter The letter to search for last names starting with.
     * @return A result for rendering volunteers.html with a list of volunteers.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public Result browse(Character letter) {

        Session session = getSession();

        Criteria criteria = session.createCriteria(Volunteer.class)
                .add( Restrictions.like("lastName", Character.toUpperCase(letter) +"%"))
                .addOrder( Order.asc("lastName"));

        List<Volunteer> volunteerList = criteria.list();

        // also need to pass the dynamic form for rendering
        DynamicForm searchForm = formFactory.form();

        return ok(volunteers.render(volunteerList, searchForm));
    }

    /**
     * An action that renders a view containing a form for capturing a new
     * volunteer.
     *
     * @return A result for rendering newvolunteer.html
     */

    public Result newVolunteer() {
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);

        return ok(newvolunteer.render(volunteerForm));
    }


    /**
     * An action that renders a view containing a form for viewing or editing
     * a volunteers's details.
     *
     * @param id The ID of the volunteer being viewed or edited.
     * @return A result for rendering volunteers.details.html or null.
     */

    @Transactional
    public Result details(int id) {
        final Volunteer volunteer = getVolunteerById(id);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %d does not exist.", id));
        }

        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> filledForm = volunteerForm.fill(volunteer);
        return ok(details.render(volunteer, filledForm));
    }


    private Volunteer getVolunteerById(int id) {
        Session session = getSession();

        // gets the Volunteer from the database or null if not found
        return session.get(Volunteer.class, id);

    }


    /**
     * An action that saves a <code>Volunteer</code> to the database.
     * <p>
     *     This method saves a <code>Volunteer</code> to the database
     *     before redirecting back to the list of all volunteers.
     * </p>
     * @return A result that redirects back to the list of volunteers.
     */
    @Transactional
    public Result save() {
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> boundForm = volunteerForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(newvolunteer.render(boundForm));
        }

        // a new volunteer is created by the form entries
        Volunteer volunteer = boundForm.get();

        // use VolunteerBuilder to handle empty, non-required fields
        Volunteer volunteer1 = buildVolunteer(volunteer);


        // use a Hibernate session to save the volunteer
        Session session = getSession();
        session.save(volunteer1);

        flash("success", String.format("Successfully added volunteer %s", volunteer));
        return redirect(routes.VolunteerController.list());
    }


    /**
     * An action that updates a <code>Volunteer</code> object based on
     * fields submitted via a form.
     * <p>
     *     This method retrieves information from a form, and uses
     *     the form data to update an existing location. If there
     *     are errors with the submitted form the user is notified.
     *     The user is also notified via flash message that the
     *     volunteer was updated.
     * </p>

     * @param id The ID of the volunteer being updated.
     * @return A result that redirects back to the list of volunteers,
     * or re-renders the form if there were errors.
     */
    @Transactional
    public Result update(int id) {
        Volunteer volunteer = getVolunteerById(id);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %d does not exist.", id));
        }

        //get the new data from the form
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> boundForm = volunteerForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(details.render(volunteer,boundForm));
        }

        //update the volunteer (probably a better way to do this)
        Volunteer updatedVolunteer = boundForm.get();

        volunteer.setFirstName(updatedVolunteer.getFirstName());
        volunteer.setLastName(updatedVolunteer.getLastName());
        volunteer.setStatus(updatedVolunteer.getStatus());
        volunteer.setEmail(updatedVolunteer.getEmail());
        volunteer.setAddress1(updatedVolunteer.getAddress1());
        volunteer.setAddress2(updatedVolunteer.getAddress2());
        volunteer.setCity(updatedVolunteer.getCity());
        volunteer.setState(updatedVolunteer.getState());
        volunteer.setZipCode(updatedVolunteer.getZipCode());


        // use a Hibernate session to update the volunteer
        Session session = getSession();
        session.update(volunteer);

        flash("success", String.format("Successfully updated volunteer %s", volunteer));
        return redirect(routes.VolunteerController.list());


    }

    /**
     * An action that deletes a <code>Volunteer</code> from the database.
     *
     * @param id The ID of the volunteer being deleted.
     * @return A result that redirects back to the list of volunteers.
     */

    @Transactional
    public Result delete(int id) {
        final Volunteer volunteer = getVolunteerById(id);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %d does not exist.", id));
        }
        // remove all assignments before deleting
        removeVolunteerFromAssignments(volunteer);

        Session session = getSession();
        session.delete(volunteer);

        flash("success", String.format("Successfully deleted volunteer %s", volunteer));

        return redirect(routes.VolunteerController.list());
    }


    private void removeVolunteerFromAssignments(Volunteer volunteer) {

        Set<Assignment> assignments = volunteer.getAssignments();

        for (Assignment assignment : assignments){
            assignment.getVolunteers().remove(volunteer);
        }
    }

    /**
     * An action that searches for a <code>Volunteer</code> by first name.
     *
     * @return A result for rendering volunteers.html with a list of
     * volunteers and a the search form.
     *
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public Result searchFirstName() {
        // get the data from the form
        DynamicForm formData = formFactory.form().bindFromRequest();
        String firstName = formData.get("firstName");

        Session session = getSession();

        Criteria criteria = session.createCriteria(Volunteer.class)
                .add( Restrictions.ilike("firstName", firstName))
                .addOrder( Order.asc("lastName"));

        List<Volunteer> volunteerList = criteria.list();

        // also need to pass the dynamic form for rendering
        DynamicForm searchForm = formFactory.form();

        return ok(volunteers.render(volunteerList, searchForm));
    }

    /**
     * An action that searches for a <code>Volunteer</code> by last name.
     *
     * @return A result for rendering volunteers.html with a list of
     * volunteers and a the search form.
     *
     */

    @Transactional
    @SuppressWarnings("unchecked")
    public Result searchLastName(){
        // get the data from the form
        DynamicForm formData = formFactory.form().bindFromRequest();
        String lastName = formData.get("lastName");

        Session session = getSession();

        Criteria criteria = session.createCriteria(Volunteer.class)
                .add( Restrictions.ilike("lastName", lastName))
                .addOrder( Order.asc("firstName"));

        List<Volunteer> volunteerList = criteria.list();

        // also need to pass the dynamic form for rendering
        DynamicForm searchForm = formFactory.form();

        return ok(volunteers.render(volunteerList, searchForm));
    }

}
