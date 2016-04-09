package controllers;


import models.Volunteer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import views.html.volunteers.details;
import views.html.volunteers.volunteers;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class VolunteerController extends Controller {

    private FormFactory formFactory;
    private JPAApi jpaApi;

    // inject the JPA API and a form factory so they can be used globally
    @Inject
    public VolunteerController(JPAApi jpaApi, FormFactory formFactory) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    // lists all volunteers in the database
    @Transactional
    @SuppressWarnings("unchecked")
    public Result list() {

        EntityManager entityManager = jpaApi.em();

//        // create some Volunteer entries for testing
//        Volunteer volunteer1 = new Volunteer("Dan", "Geasling", "dan@geasling.com");
//        Volunteer volunteer2 = new Volunteer("Liz", "England", "lizard@england.com");
//        Volunteer volunteer3 = new Volunteer("Rami", "Ismael", "rami@vlambeer.com");
//        save(volunteer1);
//        save(volunteer2);
//        save(volunteer3);

        //get a list of volunteers to display
        Session session = (Session) entityManager.getDelegate();

        Criteria criteria = session.createCriteria(Volunteer.class);

        List<Volunteer> volunteerList = criteria.list();

        return ok(volunteers.render(volunteerList));
    }

    // renders a form for capturing a new volunteer
    public Result newVolunteer() {
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);

        return ok(details.render(volunteerForm));
    }

    // renders a form for displaying and editing a volunteers details
    @Transactional
    public Result details(int id) {
        final Volunteer volunteer = getVolunteerById(id);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %d does not exist.", id));
        }

        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> filledForm = volunteerForm.fill(volunteer);
        return ok(details.render(filledForm));
    }


    public Volunteer getVolunteerById(int id) {
        EntityManager entityManager = jpaApi.em();

        Session session = (Session) entityManager.getDelegate();

        // gets the Volunteer from the database or null if not found
        Volunteer volunteer = session.get(Volunteer.class, id);

        return volunteer;
    }


    // saves a new volunteer to the database using the form data
    @Transactional
    public Result save() {
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> boundForm = volunteerForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(details.render(boundForm));
        }

        // a new volunteer is created by the form entries
        Volunteer volunteer = boundForm.get();

        save(volunteer);

        flash("success", String.format("Successfully added volunteer %s", volunteer));
        return redirect(routes.VolunteerController.list());
    }

    // called when adding a new or updating an existing Volunteer
    public void save(Volunteer volunteer) {
        EntityManager entityManager = jpaApi.em();
        entityManager.persist(volunteer);
    }

    @Transactional
    public Result delete(int id) {
        final Volunteer volunteer = getVolunteerById(id);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %d does not exist.", id));
        }

        EntityManager entityManager = jpaApi.em();
        Session session = (Session) entityManager.getDelegate();
        session.delete(volunteer);

        flash("success", String.format("Successfully deleted volunteer %s", volunteer.firstName));

        return redirect(routes.VolunteerController.list());
    }


}
