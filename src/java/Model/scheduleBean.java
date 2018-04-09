/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.DB_Connection;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class scheduleBean implements Serializable {

    private Date tempDD, tempAD;
    private String tempFrom, tempTo, selected;
    Schedule TargetSchedule = new Schedule();
    private final String[] statuslist = new String[]{"Open", "Closed"};
    Ship TargetShip = new Ship();
    private List<Schedule> filteredScheduleList;

    public String[] getStatuslist() {
        return statuslist;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public Date getTempDD() {
        return tempDD;
    }

    public void setTempDD(Date tempDD) {
        this.tempDD = tempDD;
    }

    public Date getTempAD() {
        return tempAD;
    }

    public void setTempAD(Date tempAD) {
        this.tempAD = tempAD;
    }

    public String getTempFrom() {
        return tempFrom;
    }

    public void setTempFrom(String tempFrom) {
        this.tempFrom = tempFrom;
    }

    public String getTempTo() {
        return tempTo;
    }

    public void setTempTo(String tempTo) {
        this.tempTo = tempTo;
    }

    public Schedule getTargetSchedule() {
        return TargetSchedule;
    }

    public void setTargetSchedule(Schedule TargetSchedule) {
        this.TargetSchedule = TargetSchedule;
    }

    public List<Schedule> getFilteredScheduleList() {
        return filteredScheduleList;
    }

    public void setFilteredScheduleList(List<Schedule> filteredScheduleList) {
        this.filteredScheduleList = filteredScheduleList;
    }

    public Ship getTargetShip() {
        return TargetShip;
    }

    public void setTargetShip(Ship TargetShip) {
        this.TargetShip = TargetShip;
    }

    public void clearTemp() {
        setTempAD(null);
        setTempDD(null);
        setTempFrom(null);
        setTempTo(null);
        setTargetShip(null);
    }

    public List<Schedule> getAllSchedule() {
        ArrayList<Schedule> SL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM schedule_details");
            while (rs.next()) {
                Schedule S1 = new Schedule();
                S1.setSID(rs.getInt("SID"));
                S1.setDepD(rs.getDate("DepartureDate"));
                S1.setArvD(rs.getDate("ArrivalDate"));
                S1.setShipID(rs.getInt("ShipID"));
                S1.setShipName(rs.getString("ShipName"));
                S1.setShipCapacity(rs.getDouble("ShipCapacity"));
                S1.setAvailWeight(rs.getDouble("AvailableWeight"));
                S1.setFrom(rs.getString("FromRoute"));
                S1.setTo(rs.getString("ToRoute"));
                S1.setStatus(rs.getString("Status"));
                SL.add(S1);
            }
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
        return SL;
    }

    public List<Schedule> getAllOpenSchedule() {
        List<Schedule> SL = getAllSchedule();
        List<Schedule> OSL = new ArrayList();
        for (int i = 0; i < SL.size(); i++) {
            Schedule S1 = SL.get(i);
            if (S1.getStatus().equals("Open")) {
                OSL.add(S1);
            }
        }

        return OSL;
    }

    public List<Ship> getAllAvailableShip() {

        ArrayList<Ship> SL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ship_details WHERE Status = 'Available'");
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

    public void registerSchedule() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String DD = sdf.format(tempDD);  // Store as string or date??
        String AD = sdf.format(tempAD);  // try date xian!!
        Connection con = null;
        if (tempAD.before(tempDD)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid schedule date selected !", "Please ensure that the Arrival Date is on the same date as Departure Date or after it.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement stmt = con.createStatement();
                String insertSchedule = "INSERT INTO schedule_details (DepartureDate,ArrivalDate,ShipID,ShipName,ShipCapacity,AvailableWeight,FromRoute,ToRoute,Status) values ('" + DD + "','" + AD + "','" + TargetShip.getShipID() + "','" + TargetShip.getName() + "','" + TargetShip.getCapacity() + "','" + TargetShip.getCapacity() + "','" + tempFrom + "','" + tempTo + "','Open')";
                String updateShip = "UPDATE ship_details SET Status = 'Occupied' WHERE ShipID = '" + TargetShip.getShipID() + "'";
                con.setAutoCommit(false);
                stmt.addBatch(insertSchedule);
                stmt.addBatch(updateShip);
                stmt.executeBatch();
                con.commit();
                FacesMessage msg = new FacesMessage("Schedule added", "ShipID: " + TargetShip.getShipID() + " | Route: " + tempFrom + " - " + tempTo + "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                clearTemp();
                TargetShip = new Ship();
                filteredScheduleList = getAllSchedule();

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

    public void loadSelectedShip() {
        if (selected != null) {
            String[] ship = selected.split(" | ");
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM ship_details WHERE ShipID = '" + ship[0] + "'");
                if (rs.next()) {
                    System.out.println("gotleh");
                    TargetShip.setShipID(rs.getInt("ShipID"));
                    TargetShip.setName(rs.getString("Name"));
                    TargetShip.setCapacity(rs.getDouble("Capacity"));
                    TargetShip.setIMO(rs.getInt("IMO"));
                    TargetShip.setYear(rs.getInt("Year"));
                    TargetShip.setStatus(rs.getString("Status"));
                } else {
                    System.out.println("NOPE");
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ship not found !", "There are no ships available at the moment.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }

            } catch (SQLException e) {
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
}
