/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;


import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;


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
    private LinkedList<Integer> linkedlist = new LinkedList<>();

    public void setAll(ObservableList<SalesData> salesData,List<CheckBox> yearCheckboxes,List<CheckBox> vehicleCheckboxes,List<CheckBox> regionCheckboxes,List<CheckBox> quarterCheckboxes){
        List<String> selectedYears = new LinkedList<>();
        List<String> selectedQuaters = new LinkedList<>();
        List<String> selectedVehicles = new LinkedList<>();
        List<String> selectedRegions = new LinkedList<>();

        yearCheckboxes.stream().filter((box) -> (box.isSelected())).forEach((box) -> {
            selectedYears.add(box.getText());
        });
        quarterCheckboxes.stream().filter((box) -> (box.isSelected())).forEach((box) -> {
            selectedQuaters.add(box.getText());
        });
        vehicleCheckboxes.stream().filter((box) -> (box.isSelected())).forEach((box) -> {
            selectedVehicles.add(box.getText());
        });
        regionCheckboxes.stream().filter((box) -> (box.isSelected())).forEach((box) -> {
            selectedRegions.add(box.getText());
        });
                    linkedlist.clear();
        salesData.stream().forEach((s) -> {
            String year = Integer.toString(s.getYear());
            String quater = 'Q' + Byte.toString(s.getQTR());
            String region = s.getRegion();
            String vehicle = s.getVehicle();
            boolean foundYear = false;
            boolean foundVehicle = false;
            boolean foundRegion = false;
            boolean foundQuater = false;
            for(int i = 0; i < selectedYears.size(); i++){
                if(selectedYears.get(i).equals(year)){
                    foundYear = true;
                }
            }
            
            for(int i = 0; i < selectedQuaters.size(); i++){
                if(selectedQuaters.get(i).equals(quater)){
                    foundQuater = true;
                }
            }
            
            for(int i = 0; i < selectedRegions.size(); i++){
                if(selectedRegions.get(i).equals(region)){
                    foundRegion = true;
                }
            }
            
            for(int i = 0; i < selectedVehicles.size(); i++){
                if(selectedVehicles.get(i).equals(vehicle)){
                    foundVehicle = true;
                }
            }
            System.out.println(s.getQuantity() + " = " + foundYear +" : "+foundVehicle +" : "+ foundRegion  +" : "+ foundQuater);
            if (foundYear && foundVehicle && foundRegion && foundQuater) {
                    linkedlist.add(s.getQuantity());
            }
        });
        System.out.println(linkedlist);
        calculateValues();
    }
    private void calculateValues(){
        total.set(0.0);
        min.set(0.0);
        max.set(0.0);
        average.set(0.0);
        sdv.set(0.0);
        for(int i = 0; i < linkedlist.size(); i++){
        total.set(linkedlist.get(i) + total.get());
            if(max.get() < linkedlist.get(i) ){
            max.set(linkedlist.get(i));
            }
            if(min.get() > linkedlist.get(i) || min.get() == 0.0){
            min.set(linkedlist.get(i));
            }
        }         
        average.set(total.get()/ linkedlist.size());
        double totalVal = 0.0;
        for(int i = 0; i < linkedlist.size(); i++){
        totalVal += Math.pow((linkedlist.get(i) - average.get()),2);
        }
        sdv.set(Math.sqrt((totalVal/linkedlist.size())));
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
    
     public LinkedList<Integer> getLinkedlist() {
        return linkedlist;
    }

    public void setLinkedlist(LinkedList<Integer> linkedlist) {
        this.linkedlist = linkedlist;
    }

}
