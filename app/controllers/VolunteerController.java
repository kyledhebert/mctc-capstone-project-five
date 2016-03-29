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
        return TODO;
    }

    public Result save() {
        return TODO;
    }
}
