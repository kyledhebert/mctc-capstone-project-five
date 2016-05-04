package controllers;


import models.Assignment;
import models.Location;
import models.Volunteer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;


import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import views.html.assignments.addvolunteers;
import views.html.assignments.assignments;
import views.html.assignments.details;
import views.html.assignments.newassignment;

/**
 * This controller manages persistence operations for <code>Assignments</code>.
 *
 * This controller uses JPA and Hibernate to create, read, update
 * and delete <code>Assignments</code> using database transactions.
 *
 * @author Kyle D. Hebert
 * @version 0.2
 *
 */
public class AssignmentController extends Controller {

    private FormFactory formFactory;
    private JPAApi jpaApi;

    // inject the JPa aPI and FormFactory so they can be used globally
    @Inject
    public AssignmentController(JPAApi jpaApi, FormFactory formFactory) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    private Session getSession() {
        EntityManager entityManager = jpaApi.em();
        return (Session) entityManager.getDelegate();
    }

    /**
     * An action that renders an HTML view that lists all assignments in the database.
     *
     * @return A result for rendering assignments.html with a list of assignments.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public Result list() {

        Session session = getSession();

        Criteria criteria = session.createCriteria(Assignment.class);

        List<Assignment> assignmentList = criteria.list();

        return ok(assignments.render(assignmentList));
    }

    /**
     * An action that renders a view containing a form for capturing a new assignment.
     *
     * @param locationId The ID of the location the assignment belongs too
     * @return A result for rendering newassignment.html with an <code>assignmentForm</code>
     */
    public Result newAssignment(int locationId) {

        Form<Assignment> assignmentForm = formFactory.form(Assignment.class);

        // locationId gets passed so the assignment can be associated with
        // the location on saving
        return ok(newassignment.render(assignmentForm, locationId));
    }

    /**
     * An action that renders a view containing a form for viewing or editing an assignment's details.
     *
     * @param id The ID of the assignment being viewed or edited
     * @return A result for rendering details.html or null
     */
    @Transactional
    public Result details(int id) {
        final Assignment assignment = getAssignmentById(id);
        if (assignment == null) {
            return notFound(String.format("Assignment %d does not exist", id));
        }

        // creates a form and pre-fills it with the assignment details
        Form<Assignment> assignmentForm = formFactory.form(Assignment.class);
        Form<Assignment> filledForm = assignmentForm.fill(assignment);

        return ok(details.render(assignment, filledForm));
    }

    private Assignment getAssignmentById(int id) {
        Session session = getSession();

        // gets the assignment from the database or null if not found
        Assignment assignment = session.get(Assignment.class, id);

        return assignment;
    }

    /**
     * An action that saves an <code>assignment</code> to the database.
     * <p>
     *     This method saves an assignment to the database then
     *     redirects back to the location details page for which
     *     the assignment was created. Before the assignment is saved
     *     location gets added to the assignment's location Set.
     * </p>
     * @param locationId The ID of the location the assignment belongs to
     * @return A redirect result back to the owning location's details page
     */
    @Transactional
    public Result save(int locationId) {
        Form<Assignment> assignmentForm = formFactory.form(Assignment.class);
        Form<Assignment> boundForm = assignmentForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below");
            return badRequest(newassignment.render(boundForm, locationId));
        }

        // create a new assignment from the form entries
        Assignment assignment = boundForm.get();

        // save the new assignment and get its id
        Session session = getSession();
        assignment.setLocation(session.get(Location.class, locationId));
        session.save(assignment);

        flash("success", String.format("Successfully added assignment %s", assignment));

        return redirect(routes.LocationController.details(locationId));
    }


    /**
     * An action that updates an <code>Assignment</code> object based on submitted form data.
     * <p>
     *     This method retrieves information from a form, and uses
     *     the form data to update an existing assignment. If there
     *     are errors with the submitted form the user is notified.
     *     The user is also notified via flash message that the
     *     assignment was updated.
     * </p>
     * @param id The ID of the assignment being updated
     * @return A redirect back to the list of assignments
     */
    @Transactional
    public Result update(int id) {
        Assignment assignment = getAssignmentById(id);
        if (assignment == null) {
            return notFound(String.format("Assignment %d does not exist.", id));
        }

        // get the new data from the form
        Form<Assignment> assignmentForm = formFactory.form(Assignment.class);
        Form<Assignment> boundForm = assignmentForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below");
            return badRequest(details.render(assignment, boundForm));
        }

        // update the assignment
        Assignment updatedAssignment = boundForm.get();

        assignment.setAssignmentName(updatedAssignment.getAssignmentName());

        // update and save using Hibernate
        Session session = getSession();
        session.update(assignment);

        flash("success", String.format("Successfully updated assignment %s", assignment));
        return redirect(routes.AssignmentController.list());
    }

    /**
     * An action that deletes an <code>Assignment</code> from the database.
     * <p>
     *     After successful deletion of an assignment this method
     *     returns a flash message alerting the user, then redirects
     *     back to the list of assignments.
     * </p>
     * @param id The id of the assignment being deleted.
     * @return A redirect back to the list of assignments.
     */
    @Transactional
    public Result delete(int id) {
        final Assignment assignment = getAssignmentById(id);
        if (assignment == null) {
            return notFound(String.format("Assignment %d does not exist.", id));
        }

        Session session = getSession();
        session.delete(assignment);

        flash("success", String.format("Successfully deleted assignment %s", assignment));

        return redirect(routes.AssignmentController.list());
    }

    /**
     * An action that renders a form for searching for volunteers to add to an
     * assignment.
     * <p>
     *     This method returns a form and a list of volunteers for
     *     rendering via the addvolunteers.html template. The form
     *     allows users to search for and add volunteers to an assignment.
     *     Volunteers already associated with the assignment can also be
     *     removed from the assignment via this template.
     * </p>
     * @param assignmentId The id of the assignment the volunteer is being
     *                     assigned to
     * @return  An ok http result along with a list of volunteers and a
     *          search form for rendering
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public Result browseVolunteers(int assignmentId) {

        Session session = getSession();
        // get the assignment being added to
        Assignment assignment = session.get(Assignment.class, assignmentId);

        // get a list of all volunteers

        Criteria criteria = session.createCriteria(Volunteer.class);
        List<Volunteer> volunteerList = criteria.list();

        // create a dynamic form for searching
        DynamicForm searchForm = formFactory.form();

        return ok(addvolunteers.render(volunteerList, searchForm, assignment));
    }

    /**
     * An action that creates a relationship between an <code>Assignment</code> object
     * and a <code>Volunteer</code> object.
     * <p>
     *     This method gets both an assignment and a volunteer from the database,
     *     then adds the volunteer to the assignment's Set of volunteers. after
     *     saving the assignment an http redirect to the browseVolunteers()
     *     route is returned.
     * </p>
     * @param assignmentId The ID of the assignment the volunteer is being
     *                     assigned to.
     * @param volunteerId The ID of the volunteer being assigned to the
     *                    assignment
     * @return A redirect result to the browseVolunteers() route
     */
    @Transactional
    public Result addVolunteerToAssignment(int assignmentId, int volunteerId) {

        Session session = getSession();

        Assignment assignment = session.get(Assignment.class, assignmentId);
        Volunteer volunteer = session.get(Volunteer.class, volunteerId);

        assignment.getVolunteers().add(volunteer);

        session.save(assignment);

        return redirect(routes.AssignmentController.browseVolunteers(assignmentId));
    }

    /**
     * An action that deletes the relationship between an <code>Assignment</code> object and
     * a <code>Volunteer</code> object.
     * <p>
     *     This method gets both an assignment and a volunteer from
     *     the database, then removes the volunteer from the assignment's
     *     Set of volunteers. after saving the assignment an http redirect
     *     to the browseVolunteers() route is returned
     * </p>
     * @param assignmentId The ID of the assignment the volunteer is
     *                     being removed from
     * @param volunteerId The ID of the volunteer being removed from
     *                    the assignment
     * @return A redirect result to the browseVolunteers() route
     */
    @Transactional
    public Result removeVolunteerFromAssignment(int assignmentId, int volunteerId) {

        Session session = getSession();

        Assignment assignment = session.get(Assignment.class, assignmentId);
        Volunteer volunteer = session.get(Volunteer.class, volunteerId);

        assignment.getVolunteers().remove(volunteer);

        session.save(assignment);

        return redirect(routes.AssignmentController.browseVolunteers(assignmentId));
    }


}
