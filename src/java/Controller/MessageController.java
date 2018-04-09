/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.bookingBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@ManagedBean
@SessionScoped
public class MessageController implements Serializable {

    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    HttpSession httpSession = request.getSession(false);
    String AID = (String) httpSession.getAttribute("loginAgent");

    public int getAllRejectedBooking() {
        Connection con = null;
        int size = 0;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking_details WHERE Status = 'Rejected' AND AgentID = " + AID + "");
            while (rs.next()) {
                size++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return size;
    }

    public int getNewApprovedBooking() {
        Connection con = null;
        int size = 0;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking_details WHERE Status = 'Approved' AND AgentID = " + AID + " AND Checked = 'false'");
            while (rs.next()) {
                size++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return size;
    }

    public void checkedAB() {
        System.out.println("hallo"+AID);
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE booking_details SET Checked = 'true' WHERE Status = 'Approved'");

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(bookingBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
}
