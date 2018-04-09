/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class Schedule {

    private int SID, ShipID;
    private String shipName, from, to, status;
    private double shipCapacity, availWeight;
    private Date DepD, ArvD;

    public Schedule() {
    }

    public Schedule(int ShipID, String shipName, String from, String to, String status, double shipCapacity, double availWeight, Date DepD, Date ArvD) {
        this.ShipID = ShipID;
        this.shipName = shipName;
        this.from = from;
        this.to = to;
        this.status = status;
        this.shipCapacity = shipCapacity;
        this.availWeight = availWeight;
        this.DepD = DepD;
        this.ArvD = ArvD;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSID() {
        return SID;
    }

    public void setSID(int SID) {
        this.SID = SID;
    }

    public int getShipID() {
        return ShipID;
    }

    public void setShipID(int ShipID) {
        this.ShipID = ShipID;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String sName) {
        this.shipName = sName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getShipCapacity() {
        return shipCapacity;
    }

    public void setShipCapacity(double shipCapacity) {
        this.shipCapacity = shipCapacity;
    }

    public double getAvailWeight() {
        return availWeight;
    }

    public void setAvailWeight(double availWeight) {
        this.availWeight = availWeight;
    }

    public Date getDepD() {
        return DepD;
    }

    public void setDepD(Date DepD) {
        this.DepD = DepD;
    }

    public Date getArvD() {
        return ArvD;
    }

    public void setArvD(Date ArvD) {
        this.ArvD = ArvD;
    }

    @Override
    public String toString() {
        return String.format(String.valueOf(SID));
    }

}
