package com.ryanleahy.remoteusbclient;

import com.jcraft.jsch.ChannelSftp;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * Class controls UI actions and anything UI related
 * 
 * @author Ryan Leahy
 */
public class UI
{
    @FXML private AnchorPane pane; //whole window
    @FXML private ListView<String> driveView; //just the files displayer
    @FXML private Text status; //holds the status in the bottom left
    private static int rebootPresses; //how many times the reboot button was pressed
    
    /**
     * Method handles initializing the UI
     */
    @FXML
    public void initialize()
    {
        rebootPresses = 0;
        
        fileScan(); //scan system, if not connected it will act accordingly
        
        MainApp.setUI(this); //tell Main this UI is what we're using
    }
    
    /**
     * Choose files button listener.
     * Choose specific files to upload.
     * 
     * @param event is an ActionEvent
     */
    @FXML
    public void chooseFiles(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        List<File> files;
        List<String> paths;

        chooser.setTitle("File Selection");
        files = chooser.showOpenMultipleDialog(pane.getScene().getWindow()); //creates window chooser with native OS file explorer
        if(files != null) //if something was selected
            Driver.sendFiles(files); //upload them
        
        fileScan(); //rescan the file system with the new files on it and update the drive viewer

    }
    
    /**
     * Function handles the rebooting and updating UI
     * 
     * @param event is an ActionEvent
     */
    @FXML
    public void reboot(ActionEvent event)
    {
        rebootPresses++; //increase it by 1
        
        if(rebootPresses == 1) //in case the user presses the button multiple times the program only reboots once
        {
            Driver.reboot();
            unscanFiles(); //clean out driver viewer
            new CountDown("Reboot-Count-Down", this).start(); //start the countdown thread
        }
        else if(rebootPresses > 1)
        {
            new CountDownInterrupted("Reboot-Count-Down-Interrupted", this).start(); //if the user presses it more than once this thread tells them it's already restarting and prevents anything harmful from happening
        }
    }
    
    /**
     * Method returns the amount of times the reboot button has been pressed
     * 
     * @return rebootPresses
     */
    public static int getRebootPresses()
    {
        return rebootPresses;
    }
    
    /**
     * Method sets the amount of times the reboot button has been pressed, this is best for when an reboot button press has been handled.
     * If it's been handled than the amount of unhandled button presses should go down by one
     * 
     * @param amountPressed sets the local instance variable
     */
    public static void setRebootPresses(int amountPressed)
    {
        rebootPresses = amountPressed;
    }
    
    /**
     * Sets the status message of the UI in the bottom left corner.
     * 
     * @param s sets that status
     */
    public void setStatus(String s)
    {
        status.setText(s);
    }
    
    /**
     * Method handles the rescan files button press event
     * 
     * @param event is an ActionEvent
     */
    public void rescanFiles(ActionEvent event)
    {
       fileScan();
    }
    
    /**
     * Method scans the files on the USB and updates the UI with new entries
     */
    public void fileScan()
    {
        LinkedList<String> fileNames = new LinkedList<>();
        Vector<ChannelSftp.LsEntry> list;
        
        //update UI incase it's not displaying all the files for some reason
        list = Driver.updateFiles(); //get all the files on the drive already
        
        if(list != null) //if it is null than something is wrong with the connection
        {
            for(ChannelSftp.LsEntry entry : list) //traverse through it and put the file names in a linked list
            {
                if(!driveView.getItems().contains(entry.getFilename())) //add it as long as the UI already doesn't show it
                    fileNames.add(entry.getFilename());
            }
            driveView.getItems().addAll(fileNames); //give linkedlist to this thing
        
            driveView.setCellFactory(param -> new PathItem()); //honestly no idea I just copied this from the USBBackup so ask Kevin cruse
        }
        else if (!Driver.isConnected())
        {
            setStatus("Couldn't connect, is the USB plugged in?");
            unscanFiles();
        }
    }     
    
    /**
     * Method gets rid of all files displayed in the drive view window without taking any action on them
     */
    public void unscanFiles()
    {
        driveView.getItems().remove(0, driveView.getItems().size()); //when the usb is disconnected it doesn't make sense to display the files so create an empty list
        driveView.setCellFactory(param -> new PathItem()); //and update the window with it
    }
}
