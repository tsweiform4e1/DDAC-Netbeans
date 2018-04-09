package Controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Model.bookingBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@ManagedBean(name = "loginController")
@SessionScoped
public class loginController {

    private String username, password, userType = "",loginName;
    public boolean islogged = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    public String loginValidation() {
        String url = null;
        Connection con = null;
        if (userType.equals("Admin")) {
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM mladmin_account where Username = '" + username + "' and Password = '" + password + "'");
                if (rs.next()) {
                    System.out.println("got");
                    FacesContext context = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                    HttpSession httpSession = request.getSession(false);
                    httpSession.setAttribute("loginAdmin", rs.getString("AdminID"));
                    loginName = rs.getString("Name");
                    islogged = true;
                    url = "/admin/adminHome.xhtml?faces-redirect=true";

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Credentials!", "Please ensure that you have entered the correct Username and Password."));
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (userType.equals("Agent")) {
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM mlagent_account where Username = '" + username + "' and Password = '" + password + "'");
                if (rs.next()) {
                    System.out.println("got");
                    FacesContext context = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                    HttpSession httpSession = request.getSession(false);
                    httpSession.setAttribute("loginAgent", rs.getString("AgentID"));
                    httpSession.setAttribute("loginAgentName", rs.getString("Name"));
                    System.out.println(rs.getString("AgentID"));
                    loginName = rs.getString("Name");
                    islogged = true;
                    url = "/agent/agentHome.xhtml?faces-redirect=true";

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Credentials!", "Please ensure that you have entered the correct Username and Password."));
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return url;

    }

    public void showMsgs() {
        FacesContext context = FacesContext.getCurrentInstance();
        bookingBean bb = new bookingBean();
        MessageController mc = new MessageController();
        int Psize = bb.getAllPendingBooking().size();
        int Rsize = mc.getAllRejectedBooking();
        int Asize = mc.getNewApprovedBooking();
        if (userType.equals("Admin") && Psize > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "" + Psize + " pending booking request to be processed!", ""));
        }
        if (userType.equals("Agent") && Rsize > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "" + Rsize + " booking request is rejected!", ""));
        }
        if (userType.equals("Agent") && Asize > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "" + Asize + " new booking request is approved!", ""));
        }

    }

}
