/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.DB_Connection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@ManagedBean
@ViewScoped
public class bookingBean implements Serializable {

    private String selectedS, selectedI;
    private Schedule TargetSchedule = new Schedule();
    private Item TargetItem = new Item();
    private List<Booking> filteredBookingList;
    private Booking selectedBooking = new Booking();
    private final String[] statuslist = new String[]{"Approved", "Pending", "Rejected"};
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    HttpSession httpSession = request.getSession(false);
    String AID = (String) httpSession.getAttribute("loginAgent");

    public String[] getStatuslist() {
        return statuslist;
    }

    public String getSelectedS() {
        return selectedS;
    }

    public void setSelectedS(String selectedS) {
        this.selectedS = selectedS;
    }

    public Booking getSelectedBooking() {
        return selectedBooking;
    }

    public void setSelectedBooking(Booking selectedBooking) {
        this.selectedBooking = selectedBooking;
    }

    public String getSelectedI() {
        return selectedI;
    }

    public void setSelectedI(String selectedI) {
        this.selectedI = selectedI;
    }

    public List<Booking> getFilteredBookingList() {
        return filteredBookingList;
    }

    public void setFilteredBookingList(List<Booking> filteredBookingList) {
        this.filteredBookingList = filteredBookingList;
    }

    public Schedule getTargetSchedule() {
        return TargetSchedule;
    }

    public void setTargetSchedule(Schedule TargetSchedule) {
        this.TargetSchedule = TargetSchedule;
    }

    public Item getTargetItem() {
        return TargetItem;
    }

    public void setTargetItem(Item TargetItem) {
        this.TargetItem = TargetItem;
    }

    public List<Booking> getAllBooking() {
        ArrayList<Booking> ABL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking_details WHERE AgentID = " + AID + "");
            while (rs.next()) {
                Booking B1 = new Booking();
                B1.setBID(rs.getInt("BID"));
                B1.setCustID(rs.getInt("CustID"));
                B1.setItemName(rs.getString("ItemName"));
                B1.setItemID(rs.getInt("ItemID"));
                B1.setSID(rs.getInt("SID"));
                B1.setStatus(rs.getString("Status"));
                B1.setWeight(rs.getDouble("Weight"));
                B1.setRemarks(rs.getString("Remarks"));
                ABL.add(B1);
            }
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
        return ABL;
    }

    public List<Booking> getAllPendingBooking() {
        ArrayList<Booking> ABL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking_details WHERE Status = 'Pending'");
            while (rs.next()) {
                Booking B1 = new Booking();
                B1.setBID(rs.getInt("BID"));
                B1.setCustID(rs.getInt("CustID"));
                B1.setAgentID(rs.getInt("AgentID"));
                B1.setItemName(rs.getString("ItemName"));
                B1.setItemID(rs.getInt("ItemID"));
                B1.setSID(rs.getInt("SID"));
                B1.setStatus(rs.getString("Status"));
                B1.setWeight(rs.getDouble("Weight"));
                ABL.add(B1);
            }
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
        return ABL;
    }

    public void loadSelectedSchedule() {
        if (selectedS != null) {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM schedule_details WHERE SID = '" + selectedS + "'");
                if (rs.next()) {
                    System.out.println("gotleh");
                    TargetSchedule.setSID(rs.getInt("SID"));
                    TargetSchedule.setDepD(rs.getDate("DepartureDate"));
                    TargetSchedule.setArvD(rs.getDate("ArrivalDate"));
                    TargetSchedule.setShipID(rs.getInt("ShipID"));
                    TargetSchedule.setShipName(rs.getString("ShipName"));
                    TargetSchedule.setShipCapacity(rs.getDouble("ShipCapacity"));
                    TargetSchedule.setAvailWeight(rs.getDouble("AvailableWeight"));
                    TargetSchedule.setFrom(rs.getString("FromRoute"));
                    TargetSchedule.setTo(rs.getString("ToRoute"));
                    TargetSchedule.setStatus(rs.getString("Status"));
                } else {
                    System.out.println("NOPE");
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Schedule not found !", "There are no schedule available at the moment.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }

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

    public void loadSelectedItem() {
        if (selectedI != null) {
            String[] item = selectedI.split(" | ");
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM item_details WHERE ItemID = '" + item[0] + "'");
                if (rs.next()) {
                    System.out.println("gotleh");
                    TargetItem.setCustID(rs.getInt("CustID"));
                    TargetItem.setCustName(rs.getString("CustName"));
                    TargetItem.setItemID(rs.getInt("ItemID"));
                    TargetItem.setName(rs.getString("Name"));
                    TargetItem.setWeight(rs.getDouble("Weight"));
                } else {
                    System.out.println("NOPE");
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Item not found !", "There are no item unscheduled at the moment.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }

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

    public void registerBooking() {
        if (TargetSchedule.getAvailWeight() < TargetItem.getWeight()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid booking request.", "Item weight is more than the available weight for this schedule.");
            FacesContext.getCurrentInstance().addMessage("message2", msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement ps = con.createStatement();
                ps.executeUpdate("insert into booking_details (AgentID,CustID,ItemID,ItemName,Weight,SID,Status) values (" + AID + ",'" + TargetItem.getCustID() + "','" + TargetItem.getItemID() + "','" + TargetItem.getName() + "'," + TargetItem.getWeight() + ",'" + TargetSchedule.getSID() + "','Pending')");
                ps.executeUpdate("UPDATE item_details SET DeliveryStatus = 'Pending' WHERE ItemID = '" + TargetItem.getItemID() + "'");
                FacesMessage msg = new FacesMessage("Sent booking request.", "Request will be processed within 24 hours.");
                FacesContext.getCurrentInstance().addMessage("message2", msg);
                TargetItem = new Item();
                TargetSchedule = new Schedule();
                filteredBookingList = getAllBooking();

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

    public void approveBooking() {
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM schedule_details WHERE SID = '" + selectedBooking.getSID() + "'");
            double AvailW = 0;
            if (rs.next()) {
                AvailW = rs.getDouble("AvailableWeight");
            }
            if (AvailW < selectedBooking.getWeight()) {
                FacesMessage msg = new FacesMessage("Booking approval denied!", "The item weight is more than the available weight for this vessel.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                double NewAW = AvailW - selectedBooking.getWeight();
                String updateS = "";
                String approveB = "UPDATE booking_details SET Status = 'Approved', Checked = 'false' WHERE BID = '" + selectedBooking.getBID() + "'";
                if (NewAW == 0) {
                    updateS = "UPDATE schedule_details SET AvailableWeight = " + NewAW + ", Status = 'Closed'  WHERE SID = '" + selectedBooking.getSID() + "' ";
                } else {
                    updateS = "UPDATE schedule_details SET AvailableWeight = " + NewAW + " WHERE SID = '" + selectedBooking.getSID() + "' ";
                }
                String updateI = "UPDATE item_details SET DeliveryStatus = 'Scheduled', SID = '" + selectedBooking.getSID() + "' WHERE ItemID = '" + selectedBooking.getItemID() + "' ";
                con.setAutoCommit(false);
                stmt.addBatch(approveB);
                stmt.addBatch(updateS);
                stmt.addBatch(updateI);
                stmt.executeBatch();
                con.commit();
                FacesMessage msg = new FacesMessage("Booking request approved!", "BID = " + selectedBooking.getBID() + "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

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

    public void rejectBooking() {
        if (selectedBooking.getRemarks().length() < 3) {
            FacesMessage msg = new FacesMessage("Remarks are required!", "Please fill in the reason for rejecting this booking.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement stmt = con.createStatement();
                String updateB = "UPDATE booking_details SET Status = 'Rejected', Remarks = '" + selectedBooking.getRemarks() + "' WHERE BID = " + selectedBooking.getBID() + "";
                String updateI = "UPDATE item_details SET DeliveryStatus = 'Unscheduled' WHERE ItemID = " + selectedBooking.getItemID() + "";
                con.setAutoCommit(false);
                stmt.addBatch(updateB);
                stmt.addBatch(updateI);
                stmt.executeBatch();
                con.commit();
                FacesMessage msg = new FacesMessage("Booking is rejected.", "BID = " + selectedBooking.getBID() + "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

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

    public void deleteRejectedBooking(String BID) {
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM booking_details WHERE BID = " + BID + " AND Status = 'Rejected'");
            context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Rejected booking removed from the list !", "BID: " + BID + ""));
            filteredBookingList = getAllBooking();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(itemBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
