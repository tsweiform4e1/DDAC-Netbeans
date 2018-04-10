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
public class Booking {

    private int BID, SID, AgentID, CustID, ItemID;
    private String itemName, status, remarks = "";
    private double weight;

    public Booking() {
    }

    public Booking(int SID, int AgentID, int CustID, int ItemID, String itemName, String status, String remarks, double weight) {
        this.SID = SID;
        this.AgentID = AgentID;
        this.CustID = CustID;
        this.ItemID = ItemID;
        this.itemName = itemName;
        this.status = status;
        this.remarks = remarks;
        this.weight = weight;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int ItemID) {
        this.ItemID = ItemID;
    }

    public int getBID() {
        return BID;
    }

    public void setBID(int BID) {
        this.BID = BID;
    }

    public int getSID() {
        return SID;
    }

    public void setSID(int SID) {
        this.SID = SID;
    }

    public int getAgentID() {
        return AgentID;
    }

    public void setAgentID(int AgentID) {
        this.AgentID = AgentID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int CustID) {
        this.CustID = CustID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Booking{" + "BID=" + BID + ", SID=" + SID + ", AgentID=" + AgentID + ", CustID=" + CustID + ", ItemID=" + ItemID + ", itemName=" + itemName + ", status=" + status + ", remarks=" + remarks + ", weight=" + weight + '}';
    }

}
