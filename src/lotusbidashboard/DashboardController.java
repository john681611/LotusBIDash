/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 *
 * @author Jamie
 */
public class DashboardController implements Initializable {
    
    @FXML
    private ProgressIndicator myProgressIndicator;

    @FXML
    private TableView dataTable;

    @FXML
    private Region veil;

    @FXML
    private LineChart lineChart;

    @FXML
    private BarChart barChart;
    
    @FXML
    private PieChart pieChart;

    @FXML
    private CheckBox autoUpdateCheck;

    @FXML
    private Label lastUpdatedLabel;
    
    @FXML
    private VBox chartFilters;
    
    @FXML
    private HBox statsBox;
    
        @FXML
    private ChoiceBox LineXAxis;
    
    @FXML
    private ChoiceBox LineYAxis;
    
    @FXML
    private ChoiceBox BarXAxis;
    
    @FXML
    private ChoiceBox BarYAxis;
    
    @FXML 
    private ChoiceBox PieChoice;

    private final SalesService salesService = new SalesService();
    private ObservableList<Sales> data = FXCollections.observableArrayList();
    private ObservableList<Sales> filteredData = FXCollections.observableArrayList();
    private List<Integer> years;
    private List<String> vehicles;
    private List<String> regions;
    private final List<String> quarters = Arrays.asList("Q1","Q2","Q3","Q4");
    private List<CheckBox> yearCheckboxes = new ArrayList<CheckBox>();
    private List<CheckBox> vehicleCheckboxes = new ArrayList<CheckBox>();
    private List<CheckBox> regionCheckboxes = new ArrayList<CheckBox>();
    private List<CheckBox> quarterCheckboxes = new ArrayList<CheckBox>();
    private final List<String> pieChoiceList =  Arrays.asList("Vehicle","Region");
    
    private Stats stats = new Stats();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service change listener
        salesService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) -> {
            System.out.println(newState.toString());
            switch (newState) {
                case SCHEDULED:
                    break;
                case RUNNING:
                    veil.setVisible(true);
                    veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.25)");
                    veil.setOpacity(0.8);
                    myProgressIndicator.setVisible(true);
                    myProgressIndicator.progressProperty().bind(salesService.progressProperty());
                    break;
                case READY:
                case SUCCEEDED:
                case CANCELLED:
                case FAILED:
                    myProgressIndicator.progressProperty().unbind();
                    myProgressIndicator.setVisible(false);
                    veil.setVisible(false);
                    setLastUpdated();
                    break;
            }
        });

        salesService.setOnSucceeded(event -> {
            data.setAll(salesService.getValue());
        });
        
        data.addListener((ListChangeListener.Change<? extends Sales> c) -> {
                setFilters();
                createFilterCheckboxes();
                addFiltersToUI();
                generateFilteredData();

                
        });
        
        buildTable();
    
        myProgressIndicator.progressProperty().bind(salesService.progressProperty());

        if(!salesService.isRunning()){
            salesService.reset();
            salesService.start();
        }
        
        PieChoice.getItems().setAll(pieChoiceList);
        PieChoice.getSelectionModel().selectFirst();
        PieChoice.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String oldVal, String newVal) {
                buildPieChart(newVal);
            }
        });
    }  
    
     public void generateFilteredData(){
         filteredData.clear();
        data.stream().forEach((Sales s) -> {
            boolean foundYear = false;
            boolean foundVehicle = false;
            boolean foundRegion = false;
            boolean foundQuater = false;
            for (CheckBox box : yearCheckboxes) {
                if(box.isSelected() && box.getText().equals(Integer.toString(s.getYear()))){
                    foundYear = true;
                }
            }    
            if(foundYear){
                for (CheckBox box : quarterCheckboxes) {
                    if(box.isSelected() && box.getText().equals('Q'+ Integer.toString(s.getQTR()))){
                        foundQuater = true;
                    }
                }
                if(foundQuater){
                    for (CheckBox box : regionCheckboxes) {
                        if(box.isSelected() && box.getText().equals(s.getRegion())){
                            foundRegion = true;
                        }
                    }
                    if(foundRegion){
                        for (CheckBox box : vehicleCheckboxes) {
                            if(box.isSelected() && box.getText().equals(s.getVehicle())){
                                foundVehicle = true;
                            }
                        }
                    }
                }
            }
            //System.out.println(s + " = " + foundYear +" : "+foundVehicle +" : "+ foundRegion  +" : "+ foundQuater);
            if (foundYear && foundVehicle && foundRegion && foundQuater) {
                filteredData.add(s);
            }         
        });
        updateStats();
        buildBarChart();
        bindTable();
        buildPieChart(PieChoice.valueProperty().getValue().toString());
    }
    
//    private void buildBarChart(ObservableList<Sales> filteredData) {
//        //clear charts
//        barChart.getData().clear();
//
//        years.stream().map((year) -> {
//            XYChart.Series series = new XYChart.Series<String,Number>();
//            series.setName(Integer.toString(year));
//            filteredData.stream().filter(o -> o.getYear() == year).forEach(o -> {series.getData().add(new XYChart.Data<String,Number>(o.getVehicle(), o.getQuantity()));});
//            return series;
//        }).forEach((series) -> {
//            barChart.getData().add(series);
//        });
//    }
    private void buildBarChart() {
        //clear charts
        barChart.getData().clear();
        
        years.stream().map((year) -> {
            XYChart.Series series = new XYChart.Series();
            series.setName(Integer.toString(year));          
            Map<String, Integer> totalSalesByYear = filteredData.stream()
                    .filter(o -> o.getYear() == year)
                        .collect(Collectors.groupingBy(Sales::getVehicle, Collectors.reducing(0, Sales::getQuantity, Integer::sum)));
            
            totalSalesByYear.entrySet().stream().forEach((entry) -> {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            });
            return series;
        }).forEach((series) -> {
            barChart.getData().add(series);
        });
    }
    private void buildPieChart(String item){
        pieChart.getData().clear();
        Map<String,Integer> list = null;
        System.out.println(PieChoice.getSelectionModel().getSelectedItem());
        if("Vehicle".equals(item)) {
              list = filteredData.stream().collect(Collectors.groupingBy(Sales::getVehicle, Collectors.reducing(0, Sales::getQuantity, Integer::sum)));
        }else{
              list = filteredData.stream().collect(Collectors.groupingBy(Sales::getRegion, Collectors.reducing(0, Sales::getQuantity, Integer::sum)));
        }
             list.entrySet().stream().forEach((entry) -> {
                 PieChart.Data pieData = new  PieChart.Data("" , entry.getValue());
                 pieData.setName(entry.getKey() + ": "+ entry.getValue().toString());
                 pieChart.getData().add(pieData);

        });
    }



    private void bindTable() {
        dataTable.getItems().clear();
        dataTable.getItems().addAll(filteredData);
    }

    private void setLastUpdated() {
        lastUpdatedLabel.setText(String.format("Last Updated: %s", new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss").format(new Date())));
    }

    public void onAutoUpdateCheckChanged(ActionEvent actionEvent) {
        if(autoUpdateCheck.isSelected()){
            if(!salesService.isRunning()){
                salesService.reset();
                salesService.start();
            }
        }else{
            salesService.cancel();
        }
    }

    private void setFilters() {
        years = data.stream().map(o -> o.getYear()).distinct().collect(Collectors.toList());
        vehicles = data.stream().map(o -> o.getVehicle()).distinct().collect(Collectors.toList());
        regions = data.stream().map(o -> o.getRegion()).distinct().collect(Collectors.toList());
    }

    private void createFilterCheckboxes() {
        List<CheckBox> temp;
        temp = yearCheckboxes;        
        yearCheckboxes = buildCheckboxes(temp, years);
        
        //quarters
        temp = quarterCheckboxes;
        quarterCheckboxes = buildCheckboxes(temp, quarters);
        
        //vehicles
        temp = vehicleCheckboxes;
        vehicleCheckboxes = buildCheckboxes(temp, vehicles);
        
        //regions
        temp = regionCheckboxes;
        regionCheckboxes = buildCheckboxes(temp, regions);
    }

    private void addFiltersToUI() {
        chartFilters.getChildren().clear();
        chartFilters.getChildren().add(addLabel("Year:"));
        chartFilters.getChildren().add(filterHBox(yearCheckboxes));
        chartFilters.getChildren().add(addLabel("Quater:"));
        chartFilters.getChildren().add(filterHBox(quarterCheckboxes));
        chartFilters.getChildren().add(addLabel("Vehicle:"));
        chartFilters.getChildren().add(filterHBox(vehicleCheckboxes));
        chartFilters.getChildren().add(addLabel("Region:"));
        chartFilters.getChildren().add(filterHBox(regionCheckboxes));
    }
    
    private HBox filterHBox(List<CheckBox> checkBoxes){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(10);
        checkBoxes.stream().forEach((cb) -> {
            hbox.getChildren().add(cb);
        });
        return hbox;
    }
    private Label addLabel (String name){
      Label label = new Label();
        label.setText(name);
        return label;
    }

    private List<CheckBox> buildCheckboxes(List<CheckBox> cbList, List list) {
        List<CheckBox> checkBoxes = new ArrayList<CheckBox>();

        for (byte index = 0; index < list.size(); index++) {
            CheckBox cb = new CheckBox(list.get(index).toString());
            cb.setSelected(true);
            cb.setOnAction(e ->{
                generateFilteredData();
            });
            checkBoxes.add(cb);
        }
        
        if(cbList.size() > 0){
            cbList.stream().forEach((origCb) -> {
                checkBoxes.stream().filter((newCb) -> (newCb.getText().equals(origCb.getText()))).forEach((newCb) -> {
                    newCb.setSelected(origCb.isSelected());
                });
            });
        }
        
        return checkBoxes;
    }

    private void buildTable() {
        TableColumn<Sales,Integer> yearCol = new TableColumn<>();
        yearCol.setText("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("Year"));
        TableColumn<Sales,Integer> qtrCol = new TableColumn<>();
        qtrCol.setText("Quarter");
        qtrCol.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("QTR"));
        TableColumn modelCol = new TableColumn();
        modelCol.setText("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory("Vehicle"));
        TableColumn regionCol = new TableColumn();
        regionCol.setText("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory("Region"));
        TableColumn<Sales,Integer> salesCol = new TableColumn<>();
        salesCol.setText("Sales");
        salesCol.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("Quantity"));
        
        dataTable.getColumns().addAll(yearCol,qtrCol,modelCol,regionCol,salesCol);
        //dataTable.itemsProperty().bind(salesService.valueProperty());
    }
    //Get stats data
    private void updateStats(){
    stats.setAll(filteredData);
    statsBox.getChildren().clear();
    statsBox.getChildren().add(addLabel("Max = "+ stats.getMax()));
    statsBox.getChildren().add(addLabel("Min = "+ stats.getMin()));
    statsBox.getChildren().add(addLabel("Total = "+ stats.getTotal()));
    statsBox.getChildren().add(addLabel("Average = "+ String.format("%.2f",stats.getAverage())));
    statsBox.getChildren().add(addLabel("Standard Deviation = "+ String.format("%.2f", stats.getSdv())));
    }
    
    @FXML void showAbout() throws IOException {
        System.out.println("doLogIn");
        Stage stage  = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AboutFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Lotus Dashboard About");
        stage.show();
    }
    @FXML void showSettings() throws IOException {
        System.out.println("doLogIn");
        Stage stage  = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AboutFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Lotus Dashboard About");
        stage.show();
    }
}
