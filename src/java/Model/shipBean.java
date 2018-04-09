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
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Admin
 */
@ManagedBean
@ViewScoped
public class shipBean implements Serializable {

    private String tempN;
    private int tempIMO, tempYear;
    private double tempCapacity;
    private List<Ship> filteredShipList;
    private final String[] statuslist = new String[]{"Available", "Occupied"};
    private boolean btnb;
    private Ship TargetShip = new Ship();

    public List<Ship> getFilteredShipList() {
        return filteredShipList;
    }

    public void setFilteredShipList(List<Ship> filteredShipList) {
        this.filteredShipList = filteredShipList;
    }

    public String[] getStatuslist() {
        return statuslist;
    }

    public Ship getTargetShip() {
        return TargetShip;
    }

    public void setTargetShip(Ship TargetShip) {
        this.TargetShip = TargetShip;
    }

    public String getTempN() {
        return tempN;
    }

    public void setTempN(String tempN) {
        this.tempN = tempN;
    }

    public int getTempIMO() {
        return tempIMO;
    }

    public void setTempIMO(int tempIMO) {
        this.tempIMO = tempIMO;
    }

    public int getTempYear() {
        return tempYear;
    }

    public void setTempYear(int tempYear) {
        this.tempYear = tempYear;
    }

    public double getTempCapacity() {
        return tempCapacity;
    }

    public void setTempCapacity(double tempCapacity) {
        this.tempCapacity = tempCapacity;
    }

    public int getCurrentYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return year;
    }

    public void clearTarget() {
        TargetShip.setShipID(0);
        TargetShip.setName(null);
        TargetShip.setIMO(0);
        TargetShip.setYear(0);
        TargetShip.setCapacity(0);
        TargetShip.setStatus(null);
    }

    public void clearTemp() {
        setTempN(null);
        setTempIMO(0);
        setTempYear(0);
        setTempCapacity(0);
    }

    public List<Ship> getAllShip() {

        ArrayList<Ship> SL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ship_details");
            while (rs.next()) {
                Ship S1 = new Ship();
                S1.setShipID(rs.getInt("ShipID"));
                S1.setName(rs.getString("Name"));
                S1.setIMO(rs.getInt("IMO"));
                S1.setYear(rs.getInt("Year"));
                S1.setCapacity(rs.getDouble("Capacity"));
                S1.setStatus(rs.getString("Status"));
                SL.add(S1);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(shipBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return SL;
    }

    public void registerShip() {

        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement ps = con.createStatement();
            ps.executeUpdate("insert into ship_details (Name,IMO,Year,Capacity,Status) values ('" + tempN + "','" + tempIMO + "','" + tempYear + "','" + tempCapacity + "','Available')");
            FacesMessage msg = new FacesMessage("Ship details added", "Ship Name: " + tempN + "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            clearTemp();
            filteredShipList = getAllShip();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(shipBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void deleteShip() {
        if (TargetShip.getName() == null || TargetShip.getName().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid delete ship process !", "Please search for a ship to delete.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st1 = con.createStatement();
                ResultSet rs1 = st1.executeQuery("SELECT * FROM ship_details WHERE ShipID = '" + TargetShip.getShipID() + "'");
                if (rs1.next()) {
                    Statement ps = con.createStatement();
                    ps.executeUpdate("DELETE FROM ship_details WHERE ShipID = '" + TargetShip.getShipID() + "' ");
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("Ship details are deleted successfully !", "ShipID: " + TargetShip.getShipID() + ""));
                    clearTarget();
                    filteredShipList = getAllShip();
                    btnb = false;
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This ship is no longer available !", "Please retry with another ShipID.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }

            } catch (SQLException e) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid delete ship process !", "This ship is occupied for a schedule.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(shipBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void searchShip(String targetID) {
        if (btnb) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid search process !", "You are in the middle of editing a ship details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM ship_details WHERE ShipID = '" + targetID + "'");
                if (rs.next()) {
                    System.out.println("got");
                    TargetShip.setShipID(rs.getInt("ShipID"));
                    TargetShip.setName(rs.getString("Name"));
                    TargetShip.setCapacity(rs.getDouble("Capacity"));
                    TargetShip.setIMO(rs.getInt("IMO"));
                    TargetShip.setYear(rs.getInt("Year"));
                    TargetShip.setStatus(rs.getString("Status"));
                } else {
                    System.out.println("NOPE");
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ship not found !", "Please ensure that the ShipID is entered correctly.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    clearTarget();
                }

            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(shipBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public boolean isBtnb() {
        return btnb;
    }

    public void switchState() {
        if (TargetShip.getShipID() < 1) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid edit details process !", "Please search for a ship to edit details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            btnb = true;
        }
    }

    public void editShip() {
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT * FROM ship_details WHERE ShipID = '" + TargetShip.getShipID() + "'");
            if (rs1.next()) {
                Statement ps = con.createStatement();
                ps.executeUpdate("UPDATE ship_details SET Name = '" + TargetShip.getName() + "', IMO = " + TargetShip.getIMO() + ", Year = " + TargetShip.getYear() + ", Capacity = " + TargetShip.getCapacity() + " WHERE ShipID = '" + TargetShip.getShipID() + "' ");
                FacesMessage msg = new FacesMessage("Ship details are edited successfully !", "ShipID: " + TargetShip.getShipID() + "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                filteredShipList = getAllShip();
                btnb = false;
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This ship is no longer available !", "Please retry with another ShipID.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {

            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(shipBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
