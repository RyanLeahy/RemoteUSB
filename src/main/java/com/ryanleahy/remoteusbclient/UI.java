/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import java.io.File;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * Class controls UI actions and anything UI related
 * 
 * @author rplea
 */
public class UI
{
    @FXML private AnchorPane pane;
    
    /**
     * 
     */
    @FXML
    public void initialize()
    {
        MainApp.setUI(this);
    }
    
    /**
     *  Choose files button listener.
     *  Choose specific files to backup.
     */
    @FXML
    public void chooseFiles(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        List<File> files;
        List<String> paths;

        chooser.setTitle("File Selection");
        files = chooser.showOpenMultipleDialog(pane.getScene().getWindow());
        if(files != null)
            Driver.sendFiles(files);
    }
    
    public void exit(ActionEvent event)
    {
        Driver.disconnect();
        Platform.exit();
    }
}
