package Model;

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
@ManagedBean
@ViewScoped
public class agentBean {

    private String tempUN, tempPW, tempN, btnName;
    private final String[] statuslist = new String[]{"Active", "Inactive"};
    private List<Agent> filteredAgentList;
    private boolean btnb;
    private Agent TargetAgent = new Agent();

    public String[] getStatuslist() {
        return statuslist;
    }

    public String getTempUN() {
        return tempUN;
    }

    public void setTempUN(String tempUN) {
        this.tempUN = tempUN;
    }

    public String getTempPW() {
        return tempPW;
    }

    public void setTempPW(String tempPW) {
        this.tempPW = tempPW;
    }

    public String getTempN() {
        return tempN;
    }

    public void setTempN(String tempN) {
        this.tempN = tempN;
    }

    public List<Agent> getFilteredAgentList() {
        return filteredAgentList;
    }

    public void setFilteredAgentList(List<Agent> filteredAgentList) {
        this.filteredAgentList = filteredAgentList;
    }

    public Agent getTargetAgent() {
        return TargetAgent;
    }

    public void setTargetAgent(Agent TargetAgent) {
        this.TargetAgent = TargetAgent;
    }

    private void clearTemp() {
        setTempUN(null);
        setTempPW(null);
        setTempN(null);
    }

    public void clearTarget() {
        TargetAgent.setAgentID(0);
        TargetAgent.setName(null);
        TargetAgent.setPassword(null);
        TargetAgent.setStatus(null);
        TargetAgent.setUsername(null);
    }

    public List<Agent> getAllAgent() {
        ArrayList<Agent> AL = new ArrayList();
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mlagent_account");
            while (rs.next()) {
                Agent A1 = new Agent();
                A1.setAgentID(rs.getInt("AgentID"));
                A1.setName(rs.getString("Name"));
                A1.setUsername(rs.getString("Username"));
                A1.setPassword(rs.getString("Password"));
                A1.setStatus(rs.getString("Status"));
                AL.add(A1);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(agentBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return AL;
    }

    public void registerAgent() {
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement ps = con.createStatement();
            ps.executeUpdate("insert into mlagent_account (Username,Password,Name,Status) values ('" + tempUN + "','" + tempPW + "','" + tempN + "','Active')");
            FacesMessage msg = new FacesMessage("Agent account added", "Username: " + tempUN + "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            clearTemp();
            filteredAgentList = getAllAgent();
            btnb = false;
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(agentBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void deleteAgent() {
        if (TargetAgent.getName() == null || TargetAgent.getName().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid delete agent process !", "Please search for an agent to delete.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st1 = con.createStatement();
                ResultSet rs1 = st1.executeQuery("SELECT * FROM mlagent_account WHERE AgentID = '" + TargetAgent.getAgentID() + "'");
                if (rs1.next()) {
                    Statement ps = con.createStatement();
                    ps.executeUpdate("DELETE FROM mlagent_account WHERE AgentID = '" + TargetAgent.getAgentID() + "' ");
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("Agent account is deleted successfully !", "AgentID: " + TargetAgent.getAgentID() + ""));
                    clearTarget();
                    filteredAgentList = getAllAgent();
                    btnb = false;
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This agent account is no longer available !", "Please retry with another AgentID.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }

            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(agentBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void searchAgent(String targetID) {
        if (btnb) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid search process !", "You are in the middle of editing an agent details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            Connection con = null;
            try {
                DB_Connection dbcon = new DB_Connection();
                con = dbcon.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM mlagent_account WHERE AgentID = '" + targetID + "'");
                if (rs.next()) {
                    System.out.println("got");
                    TargetAgent.setAgentID(rs.getInt("AgentID"));
                    TargetAgent.setName(rs.getString("Name"));
                    TargetAgent.setUsername(rs.getString("Username"));
                    TargetAgent.setPassword(rs.getString("Password"));
                    TargetAgent.setStatus(rs.getString("Status"));
                } else {
                    System.out.println("NOPE");
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Agent not found !", "Please ensure that the AgentID is entered correctly.");
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
                        Logger.getLogger(agentBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public boolean isBtnb() {
        return btnb;
    }

    public void switchState() {
        if (TargetAgent.getAgentID() < 1) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid edit details process !", "Please search for an account to edit details.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            btnb = true;
        }
    }

    public void editAgent() {
        Connection con = null;
        try {
            DB_Connection dbcon = new DB_Connection();
            con = dbcon.getConnection();
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT * FROM mlagent_account WHERE AgentID = '" + TargetAgent.getAgentID() + "'");
            if (rs1.next()) {
                Statement ps = con.createStatement();
                ps.executeUpdate("UPDATE mlagent_account SET Password = '" + TargetAgent.getPassword() + "', Name = '" + TargetAgent.getName() + "', Status = '" + TargetAgent.getStatus() + "'  WHERE AgentID = '" + TargetAgent.getAgentID() + "' ");
                FacesMessage msg = new FacesMessage("Agent account is edited successfully !", "AgentID: " + TargetAgent.getAgentID() + "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                filteredAgentList = getAllAgent();
                btnb = false;
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This agent account is no longer available !", "Please retry with another AgentID.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {

            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(agentBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
