/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author John-TY
 */
public class Port {
    
    public void ExportFile( ObservableList<Sales> list) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csv =  new FileChooser.ExtensionFilter("CSV", "*.csv");
        FileChooser.ExtensionFilter xls =  new FileChooser.ExtensionFilter("XLS", "*.xls");
        fileChooser.setTitle("Export File");
        Stage stage  = new Stage();
        fileChooser.getExtensionFilters().addAll(
                csv,xls
        );
        fileChooser.setInitialFileName("NewExport.csv");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {

            if(fileChooser.getSelectedExtensionFilter() == csv){
            try{
            FileWriter writer = new FileWriter(file);
                for(Sales sale :list){
                writer.append(Integer.toString(sale.getYear()));
                writer.append(",");
                writer.append(Integer.toString(sale.getQTR()));
                writer.append(",");
                writer.append(sale.getRegion());
                writer.append(",");
                writer.append(sale.getVehicle());
                writer.append(",");
                writer.append(Integer.toString(sale.getQuantity()));
                writer.append("\n");
                }
                writer.flush();
                writer.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }else{
                                try{
            FileWriter writer = new FileWriter(file);
                for(Sales sale :list){
                writer.append(Integer.toString(sale.getYear()));
                writer.append("\t");
                writer.append(Integer.toString(sale.getQTR()));
                writer.append("\t");
                writer.append(sale.getRegion());
                writer.append("\t");
                writer.append(sale.getVehicle());
                writer.append("\t");
                writer.append(Integer.toString(sale.getQuantity()));
                writer.append("\n");
                }
                writer.flush();
                writer.close();
                    } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            }
                
            }
    }
    
     public ObservableList<Sales> Import () {
	FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csv =  new FileChooser.ExtensionFilter("CSV", "*.csv");
        FileChooser.ExtensionFilter xls =  new FileChooser.ExtensionFilter("XLS", "*.xls");
        fileChooser.setTitle("Import File");
        Stage stage  = new Stage();
        fileChooser.getExtensionFilters().addAll(
                csv,xls
        );
        File file = fileChooser.showOpenDialog(stage);
        
	BufferedReader br = null;
	String line = "";
        String cvsSplitBy = ",";
          if(fileChooser.getSelectedExtensionFilter() != csv){cvsSplitBy = "\t";}
	
        ObservableList<Sales> list = FXCollections.observableArrayList();
	try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] log = line.split(cvsSplitBy);
                Sales sale = new Sales();
                sale.setYear(Integer.parseInt(log[0]));
                sale.setQTR(Integer.parseInt(log[1]));
                sale.setRegion(log[2]);
                sale.setVehicle(log[3]);
                sale.setQuantity(Integer.parseInt(log[4]));
                list.add(sale);
            }
	} catch (FileNotFoundException e) {
            System.out.println("Fail: " + e);
	} catch (IOException e) {
            System.out.println("Fail: " + e);
	} finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
	}

	System.out.println("Sucess Imported: " + list.size()+ "Items");
        return list;
  }
    
}
