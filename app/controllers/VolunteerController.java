package controllers;


import models.Volunteer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import views.html.VolunteerController.details;
import views.html.VolunteerController.volunteers;

import javax.inject.Inject;

public class VolunteerController extends Controller {

    private FormFactory formFactory;


    @Inject
    public VolunteerController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result list() {
        List<Volunteer> volunteerList = Volunteer.getVolunteerList();
        return ok(volunteers.render(volunteerList));
    }

    public Result newVolunteer() {
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);

        return ok(details.render(volunteerForm));
    }

    public Result details(String idn) {
        final Volunteer volunteer = Volunteer.getVolunteerByIdn(idn);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %s does not exist.", idn));
        }

        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> filedForm = volunteerForm.fill(volunteer);
        return ok(details.render(filedForm));
    }

    public Result save() {
        Form<Volunteer> volunteerForm = formFactory.form(Volunteer.class);
        Form<Volunteer> boundForm = volunteerForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(details.render(boundForm));
        }
        Volunteer volunteer = boundForm.get();
        volunteer.save();
        flash("success", String.format("Successfully added volunteer %s", volunteer));
        return redirect(routes.VolunteerController.list());
    }

    public Result delete(String idn) {
        final Volunteer volunteer = Volunteer.getVolunteerByIdn(idn);
        if (volunteer == null) {
            return notFound(String.format("Volunteer %s does not exist.", idn));
        }
        Volunteer.removeVolunteer(volunteer);
        return redirect(routes.VolunteerController.list());
    }
}
