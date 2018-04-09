/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Admin
 */
public class Item {

    private int ItemID, CustID, AgentID, SID;
    private double weight;
    private String custName, agentName, name, delstatus;

    public Item() {
    }

    public Item(int CustID, int AgentID, int SID, double weight, String custName, String agentName, String name, String delstatus) {
        this.CustID = CustID;
        this.AgentID = AgentID;
        this.SID = SID;
        this.weight = weight;
        this.custName = custName;
        this.agentName = agentName;
        this.name = name;
        this.delstatus = delstatus;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int ItemID) {
        this.ItemID = ItemID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int CustID) {
        this.CustID = CustID;
    }

    public int getAgentID() {
        return AgentID;
    }

    public void setAgentID(int AgentID) {
        this.AgentID = AgentID;
    }

    public int getSID() {
        return SID;
    }

    public void setSID(int SID) {
        this.SID = SID;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(String delstatus) {
        this.delstatus = delstatus;
    }

    @Override
    public String toString() {
        return String.format(ItemID + " | " + name);
    }
}
