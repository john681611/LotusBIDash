/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author John-TY
 */
public class Export {
    
    public void ExportFile( ObservableList<Sales> list) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csv =  new FileChooser.ExtensionFilter("CSV", "*.csv");
        FileChooser.ExtensionFilter xls =  new FileChooser.ExtensionFilter("XLS", "*.xls");
        fileChooser.setTitle("Export CSV");
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
                writer.append("\n ");
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
    
}
