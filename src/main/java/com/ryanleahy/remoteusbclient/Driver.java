/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

/**
 * This class is really the proper entrance point for the rest of the program, all the heavy lifting will be done here
 * 
 * @author rplea
 */
public class Driver implements Initializable
{
    @FXML
    private Label label;
    
    private Settings mySettings;
    private SSH mySSH;
    private SFTP mySFTP;
    private Parent myRoot;
    private boolean isConnect;
    
    public Driver()
    {
        mySettings = new Settings();
        mySSH = new SSH(mySettings);
        isConnect = mySSH.connect();
        mySFTP = new SFTP(mySSH);
    }
    
    public boolean disconnect()
    {
        return mySSH.disconnect();
    }
    
    public Vector updateFiles()
    {
        return mySFTP.updateFiles();
    }
    
    public boolean sendFiles(List<File> selectFiles)
    {
        return mySFTP.sendFiles(selectFiles);
    }
    
    public void setParent(Parent root)
    {
        myRoot = root;
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) 
    {
        updateFiles();
        System.out.println(disconnect());
        label.setText("Hello World!");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        sendFiles(new FileChooser().showOpenMultipleDialog(myRoot.getScene().getWindow()));
    }    
}
