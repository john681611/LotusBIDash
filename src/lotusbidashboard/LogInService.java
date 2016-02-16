/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lotusbidashboard;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Jamie
 */
public class LogInService extends Service{
    
    private String username = "";
    private String password = "";

    @Override
    protected Task createTask() {
        return new LogInTask(username, password);
    }
    
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    
}
