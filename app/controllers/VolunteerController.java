package controllers;


import models.Volunteer;
import org.hibernate.SessionFactory;
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


    public Result list() {
        List<Volunteer> volunteerList = Volunteer.getVolunteerList();
        return ok(volunteers.render(volunteerList));
    }

    // renders a form for capturing a new volunteer
    public Result newVolunteer() {
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);

        return ok(details.render(volunteerForm));
    }

    // renders a form for displaying and editing a volunteers details
    public Result details(int id) {
        final Volunteer volunteer = Volunteer.getVolunteerById(id);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %d does not exist.", id));
        }

        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> filedForm = volunteerForm.fill(volunteer);
        return ok(details.render(filedForm));
    }

    // saves a new volunteer to the database
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

        EntityManager entityManager = jpaApi.em();

        // and saved to the db via Hibernate
        entityManager.persist(volunteer);


        flash("success", String.format("Successfully added volunteer %s", volunteer));
        return redirect(routes.VolunteerController.list());
    }

    public Result delete(int id) {
        final Volunteer volunteer = Volunteer.getVolunteerById(id);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %d does not exist.", id));
        }
        Volunteer.removeVolunteer(volunteer);
        return redirect(routes.VolunteerController.list());
    }
}
