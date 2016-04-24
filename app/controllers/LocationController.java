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

import javax.persistence.EntityManager;
import javax.swing.*;
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

    // returns a list of all the locations in the database
    @Transactional
    @SuppressWarnings("unchecked")
    public List list() {

        Session session = getSession();

        Criteria criteria = session.createCriteria(Location.class);

         return criteria.list();

    }

    // renders a form for capturing a new location
    public Result newLocation() {
        Form<Location> locationForm = formFactory.form(Location.class);

        return ok(newlocation.render(locationForm));
    }



}
