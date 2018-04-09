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
public class Ship {

    private String name, status;
    private int ShipID, year, IMO;
    private double capacity;

    public Ship() {
    }

    public Ship(String name, String status, int year, int IMO, double capacity) {
        this.name = name;
        this.status = status;
        this.year = year;
        this.IMO = IMO;
        this.capacity = capacity;
    }

    public String getName() {        
        return name;        
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getShipID() {        
        return ShipID;
    }

    public void setShipID(int ShipID) {
        this.ShipID = ShipID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getIMO() {
        return IMO;
    }

    public void setIMO(int IMO) {
        this.IMO = IMO;
    }

    public double getCapacity() {        
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return String.format(ShipID + " | " + name);
    }
}
