/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



/**
 *
 * @author cmpjharv
 */
public class Stats {
    private DoubleProperty average = new SimpleDoubleProperty();
    private DoubleProperty max = new SimpleDoubleProperty();
    private DoubleProperty min = new SimpleDoubleProperty();
    private DoubleProperty sdv = new SimpleDoubleProperty();
    private DoubleProperty total = new SimpleDoubleProperty();
    private ObservableList<Sales> filteredData = FXCollections.observableArrayList();
    public void setAll(ObservableList<Sales> filteredData){
        this.filteredData = filteredData;
        calculateValues();
    }
 
    private void calculateValues(){
        total.set(filteredData.stream().map((val) -> val.getQuantity()).reduce(0, Integer::sum));
        min.set(filteredData.stream().map((val) -> val.getQuantity()).reduce(0, Integer::min));
        max.set(filteredData.stream().map((val) -> val.getQuantity()).reduce(0, Integer::max));        
        average.set(total.get()/ filteredData.size());
        double totalVal = filteredData.stream().map((val) -> Math.pow(val.getQuantity() - average.get(), 2)).reduce(totalVal, (accumulator, _item) -> accumulator + _item);
        sdv.set(Math.sqrt((totalVal/filteredData.size())));
    }
    //gets & sets and stuff
    public double getTotal() {
        return total.get();
    }

    public void setTotal(double value) {
        total.set(value);
    }

    public DoubleProperty TotalProperty() {
        return total;
    }

    public double getAverage() {
        return average.get();
    }

    public void setAverage(double value) {
        average.set(value);
    }

    public DoubleProperty averageProperty() {
        return average;
    }
    

    public double getMin() {
        return min.get();
    }

    public void setMin(double value) {
        min.set(value);
    }

    public DoubleProperty minProperty() {
        return min;
    }


    public double getMax() {
        return max.get();
    }

    public void setMax(double value) {
        max.set(value);
    }

    public DoubleProperty maxProperty() {
        return max;
    }
    

    public double getSdv() {
        return sdv.get();
    }

    public void setSdv(double value) {
        sdv.set(value);
    }

    public DoubleProperty sdvProperty() {
        return sdv;
    }
    

}
