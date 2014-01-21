/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities.service;

import entities.Login;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author crouch
 */
@Stateless
@Path("entities.login")
public class LoginFacadeREST extends AbstractFacade<Login> {
    @PersistenceContext(unitName = "PersonnelSkillsDBPU")
    private EntityManager em;

    public LoginFacadeREST() {
        super(Login.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Login entity) {
        super.create(entity);
    }
    
    @POST
    @Path("/create")
    @Consumes({"application/x-www-form-urlencoded", "application/xml", "application/json"})
    public void createUser(@FormParam("username") String username,
                           @FormParam("password") String password) {
        if(checkPassword(password)) {
            List<Login> users=em.createNamedQuery("Login.findAll").getResultList();
            int id = Integer.parseInt(users.get(users.size()-1).getUserid());
            id++;
            Login newUser = new Login(Integer.toString(id), username, password);
            super.create(newUser);
        } else {
            throw new WebApplicationException(Response.status(400).entity("Password must contain capital letter and number and be 8 characters long").build());
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Login entity) {
        super.edit(entity);
    }
    
    @POST
    @Path("/updatePswd")
    @Consumes({"application/x-www-form-urlencoded", "application/xml", "application/json"})
    public void updatePswd(@FormParam("username") String userid,
                           @FormParam("oldPassword") String oldPswd,
                           @FormParam("newPassword") String newPswd){
        if(checkPassword(newPswd)) {
            em.createQuery("UPDATE Login SET password='" + newPswd + "' WHERE userid=" + userid + " and "
                             + "password='" + oldPswd + "'").executeUpdate();
        } else {
            throw new WebApplicationException(Response.status(400).entity("New Password must contain capital letter and number and be 8 characters long").build());
        }
    }
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }
    
    @POST
    @Path("/rm")
    @Consumes({"application/x-www-form-urlencoded", "application/xml", "application/json"})
    public void delete(@FormParam("userid") String userid) {
        em.createQuery("DELETE FROM Skillsforusers sfu WHERE sfu.login.userid=" + userid).executeUpdate();
        em.createQuery("DELETE FROM Login l WHERE l.userid=" + userid).executeUpdate();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Login find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Login> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Login> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }
    
//    @GET
//    @Path("/gt/{skill}/{exp}")
//    @Produces({"application/xml", "application/json"})
//    public List<Login> findUserSkill(@PathParam("skill") String skill, @PathParam("exp") Integer exp) {
//        return em.createQuery("SELECT l FROM Login l, Skills s, Skillsforusers sfu WHERE l.userid = sfu.login.userid " +
//            "and s.skillsid = sfu.skills.skillsid and s.description = '" + skill + "' and sfu.skilllevel > " +exp).getResultList();
//    }
    
    //TypedQuery<String> query = em.createQuery("SELECT c.name FROM Country AS c", String.class);List<String> results = query.getResultList();
    @GET
    @Path("/gt/{skill}/{exp}")
    @Produces("text/html")
    public String findUserSkill(@PathParam("skill") String skill, @PathParam("exp") Integer exp) {
        TypedQuery<Object[]> query = em.createQuery("SELECT l.username, sfu.skilllevel FROM Login l, Skills s, Skillsforusers sfu WHERE l.userid = sfu.login.userid " +
            "and s.skillsid = sfu.skills.skillsid and s.description = '" + skill + "' and sfu.skilllevel > " +exp + "", Object[].class);
        List<Object[]> results = query.getResultList();
        String answer = "<h2> Personnel with Skill " + skill.replace("+", " ") + "<br><br><table border='1'> <tr>";
        answer = answer + "<th>USERNAME</th>" + " <th> SKILL LEVEL </th></tr>";
        for(Object[] result : results) {
            answer = answer + "<tr>";
            answer = answer + " <td>" + result[0] + "</td><td> " + result[1] + "</td></tr>";
        }
        answer = answer + "</table>";
        answer = answer + "<button onclick=\"window.history.back()\">Go Back</button>";
        return answer;
    }
    
    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private boolean checkPassword(String password) {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean isAtLeast8   = password.length() >= 8;//Checks for at least 8 characters
        boolean hasDigit   = false;
        for(int i = 0; i < password.length(); i++) {
            if(Character.isDigit(password.charAt(i))) {
                hasDigit = true;
                break;
            }
        }
    return hasUppercase && hasLowercase && isAtLeast8 && hasDigit;
    }
}
