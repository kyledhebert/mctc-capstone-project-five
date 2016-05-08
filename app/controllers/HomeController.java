package controllers;

import com.google.inject.Inject;
import org.hibernate.Session;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.persistence.EntityManager;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private JPAApi jpaApi;

    @Inject
    public HomeController(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    // returns a Hibernate session for database transactions
    private Session getSession() {
        EntityManager entityManager  = jpaApi.em();
        return (Session) entityManager.getDelegate();
    }

    // methods for retrieving stats from the database
    
    private Integer getVolunteerCount() {

        Session session = getSession();

        return ((Number) session.createQuery("select count(*) from Volunteer ").iterate().next()).intValue();
    }

    private Integer getLocationCount() {

        Session session = getSession();

        return ((Number) session.createQuery("select count(*) from Location ").iterate().next()).intValue();
    }

    private Integer getAssignmentCount() {

        Session session = getSession();

        return ((Number) session.createQuery("select count(*) from Assignment ").iterate().next()).intValue();
    }



    /**
     * An action that renders the Home page of the application.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Transactional
    public Result index() {


        int volunteerCount = getVolunteerCount();
        int locationCount = getLocationCount();
        int assignmentCount = getAssignmentCount();

        return ok(index.render(volunteerCount, locationCount, assignmentCount));
    }


}
