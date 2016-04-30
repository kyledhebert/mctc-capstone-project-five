package controllers;


import models.Assignment;
import models.Location;
import org.hibernate.Criteria;
import org.hibernate.Session;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;


import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import views.html.assignments.assignments;
import views.html.assignments.details;
import views.html.assignments.newassignment;


public class AssignmentController extends Controller {

    private FormFactory formFactory;
    private JPAApi jpaApi;

    // inject the JPA API and FormFactory so they can be used globally
    @Inject
    public AssignmentController(JPAApi jpaApi, FormFactory formFactory) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    // returns a Hibernate session for database Transactions
    private Session getSession() {
        EntityManager entityManager = jpaApi.em();
        return (Session) entityManager.getDelegate();
    }

    // list all the locations and assignments in the database
    @Transactional
    @SuppressWarnings("unchecked")
    public Result list() {

        Session session = getSession();

        Criteria criteria = session.createCriteria(Assignment.class);

        List<Assignment> assignmentList = criteria.list();

        return ok(assignments.render(assignmentList));



    }

    // renders a form for capturing a new assignment
    public Result newAssignment(int locationId) {

        Form<Assignment> assignmentForm = formFactory.form(Assignment.class);

        // locationId gets passed so the assignment can be associated with
        // the location on saving
        return ok(newassignment.render(assignmentForm, locationId));
    }

    // renders a from for displaying and editing an assignment's details
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

    // saves a new assignment to the database using the form data
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

        // save the new Assignment and get its id
        Session session = getSession();
        assignment.setLocation(session.get(Location.class, locationId));
        session.save(assignment);

//        // create a new location assignment
//        LocationAssignment locationAssignment = new LocationAssignment();
//        locationAssignment.location =  session.get(Location.class, locationId);
//        locationAssignment.assignment = session.get(Assignment.class, assignmentId);
//
//        // and save it
//        session.save(locationAssignment);

        flash("success", String.format("Successfully added assignment %s", assignment));

        return redirect(routes.LocationController.details(locationId));
    }



    // update an existing assignment
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

    // delete an existing assignment
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
}
