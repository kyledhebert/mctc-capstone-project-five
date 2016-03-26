package controllers;

import models.Volunteer;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import views.html.volunteers.volunteers;

public class Volunteers extends Controller {

    public Result list() {
        List<Volunteer> volunteerList = Volunteer.getVolunteerList();
        return ok(volunteers.render(volunteerList));
    }

    public Result newVolunteer() {
        return TODO;
    }

    public Result details(String idn) {
        return TODO;
    }

    public Result save() {
        return TODO;
    }
}
