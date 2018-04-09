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
public class itemBean implements Serializable {

    private Item TargetItem = new Item();
    private String tempIName, tempCName, btnName;
    private double tempWeight;
    private int tempCID;
    private final String[] delstatuslist = new String[]{"Pending", "Scheduled", "Unscheduled"};
    private List<Item> filteredItemList;
    private boolean btnb;
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    HttpSession httpSession = request.getSession(false);
    String AID = (String) httpSession.getAttribute("loginAgent");
    String AName = (String) httpSession.getAttribute("loginAgentName");

    public Item getTargetItem() {
        return TargetItem;
    }

    public void setTargetItem(Item TargetItem) {
        this.TargetItem = TargetItem;
    }

    public double getTempWeight() {
        return tempWeight;
    }

    public void setTempWeight(double tempWeight) {
        this.tempWeight = tempWeight;
    }

    public String getTempCName() {
        return tempCName;
    }

    public void setTempCName(String tempCName) {
        this.tempCName = tempCName;
    }

    public int getTempCID() {
        return tempCID;
    }

    public void setTempCID(int tempCID) {
        this.tempCID = tempCID;
    }

    public String[] getDelstatuslist() {
        return delstatuslist;
    }

    public String getTempIName() {
        return tempIName;
    }

    public void setTempIName(String tempIName) {
        this.tempIName = tempIName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public List<Item> getFilteredItemList() {
        return filteredItemList;
    }

    public void setFilteredItemList(List<Item> filteredItemList) {
        this.filteredItemList = filteredItemList;
    }

    public void setBtnb(boolean btnb) {
        this.btnb = btnb;
    }

    private void clearTemp() {
        setTempWeight(0);
        setTempIName(null);
        setTempCID(0);
        setTempCName(null);
    }

    public void clearTarget() {
        TargetItem.setAgentID(0);
        TargetItem.setAgentName(null);
        TargetItem.setCustID(0);
        TargetItem.setCustName(null);
        TargetItem.setDelstatus(null);
        TargetItem.setItemID(0);
        TargetItem.setName(null);
        TargetItem.setWeight(0);
    }

    public List<Item> getAllItem() {
        ArrayList<Item> IL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM item_details WHERE AgentID = " + AID + "");
            while (rs.next()) {
                Item I1 = new Item();
                I1.setItemID(rs.getInt("ItemID"));
                I1.setCustID(rs.getInt("CustID"));
                I1.setCustName(rs.getString("CustName"));
                I1.setName(rs.getString("Name"));
                I1.setWeight(rs.getDouble("Weight"));
                I1.setAgentID(rs.getInt("AgentID"));
                I1.setAgentName(rs.getString("AgentName"));
                I1.setDelstatus(rs.getString("DeliveryStatus"));
                I1.setSID(rs.getInt("SID"));
                IL.add(I1);
            }
        } catch (Exception e) {
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
        return IL;
    }

    public List<Item> getAllUnscheduledItem() {
        List<Item> IL = getAllItem();
        List<Item> UIL = new ArrayList();
        for (int i = 0; i < IL.size(); i++) {
            Item I1 = IL.get(i);
            if (I1.getDelstatus().equals("Unscheduled")) {
                UIL.add(I1);
            }
        }
        return UIL;
    }

    public void registerItem() {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement ps = con.createStatement();
                ps.executeUpdate("insert into item_details (CustID,CustName,Name,Weight,AgentID,AgentName,DeliveryStatus) values (" + tempCID + ",'" + tempCName + "','" + tempIName + "'," + tempWeight + "," + AID + ",'" + AName + "','Unscheduled')");
                FacesMessage msg = new FacesMessage("Item added", "CustID: " + tempCID + " Item Name: " + tempIName + "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                clearTemp();
                filteredItemList = getAllItem();

            } catch (Exception e) {
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

    public void deleteItem() {
        if (TargetItem.getItemID() < 1) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid delete item process !", "Please search for an item to delete.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st1 = con.createStatement();
                ResultSet rs1 = st1.executeQuery("SELECT * FROM item_details WHERE ItemID = '" + TargetItem.getItemID() + "' AND AgentID = " + AID + "");
                if (rs1.next()) {
                    Statement ps = con.createStatement();
                    ps.executeUpdate("DELETE FROM item_details WHERE ItemID = '" + TargetItem.getItemID() + "' ");
                    context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("Item is deleted successfully !", "ItemID: " + TargetItem.getItemID() + ""));
                    clearTarget();
                    filteredItemList = getAllItem();
                    btnb = false;
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Item not found !", "Please retry with another ItemID.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }

            } catch (Exception e) {
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

    public void searchItem(String targetID) {
        if (btnb) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid search process !", "You are in the middle of editing an item details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM item_details WHERE ItemID = '" + targetID + "' AND AgentID = " + AID + "");
                if (rs.next()) {
                    System.out.println("got");
                    if (rs.getString("DeliveryStatus").equals("Unscheduled")) {
                        TargetItem.setItemID(rs.getInt("ItemID"));
                        TargetItem.setCustID(rs.getInt("CustID"));
                        TargetItem.setCustName(rs.getString("CustName"));
                        TargetItem.setName(rs.getString("Name"));
                        TargetItem.setWeight(rs.getDouble("Weight"));
                        TargetItem.setAgentID(rs.getInt("AgentID"));
                        TargetItem.setAgentName(rs.getString("AgentName"));
                        TargetItem.setDelstatus(rs.getString("DeliveryStatus"));
                        TargetItem.setSID(rs.getInt("SID"));
                    } else {
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item search invalid!", "Only unscheduled items can be edited or deleted.");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        clearTarget();
                    }

                } else {
                    System.out.println("NOPE");
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Item not found !", "Please ensure that the ItemID is entered correctly.");
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
                        Logger.getLogger(itemBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void switchState() {
        if (TargetItem.getItemID() < 1) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid edit details process !", "Please search for an item to edit details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            btnb = true;
        }
    }

    public boolean isBtnb() {
        return btnb;
    }

    public void editItem() {
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT * FROM item_details WHERE ItemID = '" + TargetItem.getItemID() + "'");
            if (rs1.next()) {
                Statement ps = con.createStatement();
                ps.executeUpdate("UPDATE item_details SET Name = '" + TargetItem.getName() + "' ,Weight = " + TargetItem.getWeight() + "  WHERE ItemID = '" + TargetItem.getItemID() + "' ");
                FacesMessage msg = new FacesMessage("Item details are edited successfully !", "ItemID: " + TargetItem.getItemID() + "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                filteredItemList = getAllItem();
                btnb = false;
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Item not found !", "Please retry with another ItemID.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
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
    }

    public void searchCustomer(String targetID) {
        System.out.println(targetID + AID);
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mlcustomer_account WHERE CustID = '" + targetID + "' AND AgentID = " + AID + "");
            if (rs.next()) {
                System.out.println("got");
                if (rs.getString("Status").equals("Inactive")) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Customer account is Inactive !", "Please ensure that the customer account is active before adding item.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    setTempCID(0);
                    setTempCName(null);
                } else {
                    setTempCID(rs.getInt("CustID"));
                    setTempCName(rs.getString("Name"));
                    System.out.println(tempCID + tempCName);
                }
            } else {
                System.out.println("NOPE");
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer not found !", "Please ensure that the CustID is entered correctly.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                setTempCID(0);
                setTempCName(null);
            }

        } catch (Exception e) {
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
