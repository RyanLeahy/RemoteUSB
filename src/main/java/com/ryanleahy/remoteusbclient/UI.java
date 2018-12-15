/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import com.jcraft.jsch.ChannelSftp;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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
    @FXML private ListView<String> driveView;
    
    /**
     * 
     */
    @FXML
    public void initialize()
    {
        LinkedList<String> fileNames = new LinkedList<>();
        Vector<ChannelSftp.LsEntry> list;
        
        list = Driver.updateFiles(); //get all the files on the drive already
        
        for(ChannelSftp.LsEntry entry : list) //traverse through it and put the file names in a linked list
            fileNames.add(entry.getFilename());
        driveView.getItems().addAll(fileNames); //give linkedlist to this thing
        
        driveView.setCellFactory(param -> new PathItem()); //honestly no idea I just copied this from the USBBackup so ask Kevin cruse
        
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
