package com.ryanleahy.remoteusbclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable 
{
    @FXML
    private Label label;
    private Driver driver;
    
    @FXML
    private void handleButtonAction(ActionEvent event) 
    {
        driver.updateFiles();
        System.out.println(driver.disconnect());
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        driver = new Driver(); //main entrance point that handles most of the program except for UI interactions
    }    
}
