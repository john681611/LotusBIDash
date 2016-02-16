/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;

import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 *
 * @author Jamie
 */
public class SalesService extends ScheduledService<ObservableList<SalesData>> {

    private static final Duration DELAY = new Duration(30000);


    public SalesService(){
        this.setPeriod(DELAY);
    }


    @Override
    protected Task<ObservableList<SalesData>> createTask() {
        return new SalesTask();
    }
}
