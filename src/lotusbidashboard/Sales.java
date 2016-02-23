/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jamie
 */
public class Sales {
    
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final IntegerProperty quarter = new SimpleIntegerProperty();
    private final StringProperty region = new SimpleStringProperty();
    private final StringProperty vehicle = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();

    public void setYear(int year){
        this.year.set(year);
    }

    public int getYear(){
        return this.year.get();
    }
    
    public IntegerProperty yearProperty(){
        return this.year;
    }

    public void setQuarter(byte quarter){
        this.quarter.set(quarter);
    }

    public int getQuarter(){
        return this.quarter.get();
    }
    
    public IntegerProperty quarterProperty(){
        return this.quarter;
    }

    public void setRegion(String region){
        this.region.set(region);
    }

    public String getRegion(){
        return this.region.get();
    }
    
    public StringProperty regionProperty(){
        return this.region;
    }

    public void setVehicle(String vehicle){
        this.vehicle.set(vehicle);
    }

    public String getVehicle(){
        return this.vehicle.get();
    }
    
    public StringProperty vehicleProperty(){
        return this.vehicle;
    }

    public void setQuantity(int sales){
        this.quantity.set(sales);
    }

    public int getQuantity(){
        return this.quantity.get();
    }
    
    public IntegerProperty quantityProperty(){
        return this.quantity;
    }
}
