/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Customer;
import Controller.DB_Connection;
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
public class customerBean {

    private String tempN, tempG, tempEM, tempA, btnName;
    private final String[] statuslist = new String[]{"Active", "Inactive"};
    private final String[] genderlist = {"Male", "Female"};
    private List<Customer> filteredCustomerList;
    private boolean btnb;
    private Customer TargetCust = new Customer();
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    HttpSession httpSession = request.getSession(false);
    String AID = (String) httpSession.getAttribute("loginAgent");

    public String[] getGenderlist() {
        return genderlist;
    }

    public String[] getStatuslist() {
        return statuslist;
    }

    public String getTempG() {
        return tempG;
    }

    public void setTempG(String tempG) {
        this.tempG = tempG;
    }

    public String getTempEM() {
        return tempEM;
    }

    public void setTempEM(String tempEM) {
        this.tempEM = tempEM;
    }

    public String getTempA() {
        return tempA;
    }

    public void setTempA(String tempA) {
        this.tempA = tempA;
    }

    public String getTempN() {
        return tempN;
    }

    public void setTempN(String tempN) {
        this.tempN = tempN;
    }

    public List<Customer> getFilteredCustomerList() {
        return filteredCustomerList;
    }

    public void setFilteredCustomerList(List<Customer> filteredCustomerList) {
        this.filteredCustomerList = filteredCustomerList;
    }

    public Customer getTargetCust() {
        return TargetCust;
    }

    public void setTargetCust(Customer TargetCust) {
        this.TargetCust = TargetCust;
    }

    private void clearTemp() {
        setTempG(null);
        setTempEM(null);
        setTempN(null);
        setTempA(null);
    }

    public void clearTarget() {
        TargetCust.setAddress(null);
        TargetCust.setCustID(0);
        TargetCust.setEmail(null);
        TargetCust.setGender(null);
        TargetCust.setName(null);
        TargetCust.setStatus(null);
    }

    public List<Customer> getAllCustomer() {
        ArrayList<Customer> CL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mlcustomer_account WHERE AgentID = " + AID + "");
            while (rs.next()) {
                Customer C1 = new Customer();
                C1.setCustID(rs.getInt("CustID"));
                C1.setName(rs.getString("Name"));
                C1.setGender(rs.getString("Gender"));
                C1.setEmail(rs.getString("Email"));
                C1.setAddress(rs.getString("Address"));
                C1.setStatus(rs.getString("Status"));
                CL.add(C1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(customerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return CL;
    }

    public void registerCustomer() {
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement ps = con.createStatement();
            ps.executeUpdate("insert into mlcustomer_account (Name,Gender,Email,Address,Status,AgentID) values ('" + tempN + "','" + tempG + "','" + tempEM + "','" + tempA + "','Active'," + AID + ")");
            FacesMessage msg = new FacesMessage("Customer account added", "Name: " + tempN + "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            clearTemp();
            filteredCustomerList = getAllCustomer();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(customerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void deleteCustomer() {
        if (TargetCust.getName() == null || TargetCust.getName().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid delete customer process !", "Please search for a customer to delete.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st1 = con.createStatement();
                ResultSet rs1 = st1.executeQuery("SELECT * FROM mlcustomer_account WHERE CustID = '" + TargetCust.getCustID() + "' AND AgentID = " + AID + "");
                if (rs1.next()) {
                    Statement ps = con.createStatement();
                    ps.executeUpdate("DELETE FROM mlcustomer_account WHERE CustID = '" + TargetCust.getCustID() + "' ");
                    context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("Customer account is deleted successfully !", "CustID: " + TargetCust.getCustID() + ""));
                    clearTarget();
                    filteredCustomerList = getAllCustomer();
                    btnb = false;
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer account not found !", "Please retry with another CustID.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }

            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(customerBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void searchCustomer(String targetID) {
        if (btnb) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid search process !", "You are in the middle of editing a customer details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM mlcustomer_account WHERE CustID = '" + targetID + "' AND AgentID = " + AID + "");
                if (rs.next()) {
                    System.out.println("got");
                    TargetCust.setCustID(rs.getInt("CustID"));
                    TargetCust.setName(rs.getString("Name"));
                    TargetCust.setGender(rs.getString("Gender"));
                    TargetCust.setEmail(rs.getString("Email"));
                    TargetCust.setAddress(rs.getString("Address"));
                    TargetCust.setStatus(rs.getString("Status"));
                } else {
                    System.out.println("NOPE");
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer not found !", "Please ensure that the CustID is entered correctly.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    clearTarget();
                }

            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(customerBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void switchState() {
        if (TargetCust.getCustID() < 1) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid edit details process !", "Please search for an account to edit details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            btnb = true;
        }
    }

    public boolean isBtnb() {
        return btnb;
    }

    public void editCustomer() {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st1 = con.createStatement();
                ResultSet rs1 = st1.executeQuery("SELECT * FROM mlcustomer_account WHERE CustID = '" + TargetCust.getCustID() + "'");
                if (rs1.next()) {
                    Statement ps = con.createStatement();
                    ps.executeUpdate("UPDATE mlcustomer_account SET Name = '" + TargetCust.getName() + "' ,Gender = '" + TargetCust.getGender() + "', Email = '" + TargetCust.getEmail() + "',Address = '" + TargetCust.getAddress() + "', Status = '" + TargetCust.getStatus() + "'  WHERE CustID = '" + TargetCust.getCustID() + "' ");
                    FacesMessage msg = new FacesMessage("Customer account is edited successfully !", "CustID: " + TargetCust.getCustID() + "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    filteredCustomerList = getAllCustomer();
                    btnb = false;
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer account not found !", "Please retry with another CustID.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            } catch (SQLException e) {

                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(customerBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }    

}
