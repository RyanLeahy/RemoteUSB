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
import javafx.scene.text.Text;
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
    @FXML private Text status;
    private static int rebootPresses;
    
    /**
     * 
     */
    @FXML
    public void initialize()
    {
        rebootPresses = 0;
        
        fileScan();
        
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
        
        fileScan();

    }
    
    /**
     * Function handles the rebooting and updating UI
     * @param event 
     */
    @FXML
    public void reboot(ActionEvent event)
    {
        rebootPresses++; //increase it by 1
        
        if(rebootPresses == 1) //in case the user presses the button multiple times the program only reboots once
        {
            Driver.reboot();
            unscanFiles();
            new CountDown("Reboot-Count-Down", this).start();
        }
        else if(rebootPresses > 1)
        {
            new CountDownInterrupted("Reboot-Count-Down-Interrupted", this).start();
        }
    }
    
    public static int getRebootPresses()
    {
        return rebootPresses;
    }
    
    public static void setRebootPresses(int amountPressed)
    {
        rebootPresses = amountPressed;
    }
    
    public void setStatus(String s)
    {
        status.setText(s);
    }
    
    public void rescanFiles(ActionEvent event)
    {
       fileScan();
    }
    
    public void fileScan()
    {
        LinkedList<String> fileNames = new LinkedList<>();
        Vector<ChannelSftp.LsEntry> list;
        
        //update UI incase it's not displaying all the files for some reason
        list = Driver.updateFiles(); //get all the files on the drive already
        
        if(list != null)
        {
            for(ChannelSftp.LsEntry entry : list) //traverse through it and put the file names in a linked list
            {
                if(!driveView.getItems().contains(entry.getFilename())) //add it as long as the UI already doesn't show it
                    fileNames.add(entry.getFilename());
            }
            driveView.getItems().addAll(fileNames); //give linkedlist to this thing
        
            driveView.setCellFactory(param -> new PathItem()); //honestly no idea I just copied this from the USBBackup so ask Kevin cruse
        }
        else
        {
            setStatus("Couldn't establish connection, is the USB plugged in?");
            unscanFiles();
        }
    }
    
    public void unscanFiles()
    {
        driveView.getItems().remove(0, driveView.getItems().size()); //when the usb is disconnected it doesn't make sense to display the files
        driveView.setCellFactory(param -> new PathItem());
    }
}
