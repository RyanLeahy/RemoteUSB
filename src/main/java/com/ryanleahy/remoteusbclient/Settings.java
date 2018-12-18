package com.ryanleahy.remoteusbclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class reads a file and makes the settings accessible to the rest of the program
 * 
 * @author Ryan Leahy
 */
public class Settings
{
    private String myAddress;
    private String myUsername;
    private String myPassword;
    private int myPort;
    private Scanner settingsFileReader;
    private static List<String> ignoredFiles;
    
    /**
     * Constructor reads file and stores them in instance variables
     */
    public Settings()
    {
        String filePath;
        ignoredFiles = new LinkedList<String>();

        try
        {
            settingsFileReader = new Scanner(new FileReader(new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().toString() + "/settings.txt"));
        }
        catch (URISyntaxException | FileNotFoundException ex)
        {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        myAddress = settingsFileReader.nextLine().substring(3);
        myUsername = settingsFileReader.nextLine().substring(9);
        myPassword = settingsFileReader.nextLine().substring(9);
        myPort = Integer.parseInt(settingsFileReader.nextLine().substring(5));
        settingsFileReader.nextLine(); //skip text saying files to skip
        while(settingsFileReader.hasNextLine())
        {
            filePath = settingsFileReader.nextLine(); //read file paths, will exit once it throws an IOException
            ignoredFiles.add(filePath);
        }
            
        settingsFileReader.close();
        
    }
    
    /**
     * Method returns IP address of USB device
     * 
     * @return myAddress
     */
    public String getAddress()
    {
        return myAddress;
    }
    
    /**
     * Method returns Username used to login to the USB device
     * 
     * @return myUsername
     */
    public String getUsername()
    {
        return myUsername;
    }
    
    /**
     * Method returns Password used to login to the USB device
     * 
     * @return myPassword
     */
    public String getPassword()
    {
        return myPassword;
    }
    
    /**
     * Method returns Port used to connect to the USB device
     * 
     * @return myPort
     */
    public int getPort()
    {
        return myPort;
    }
    
    /**
     * Method returns list of file paths of files to ignore in the UI
     * 
     * @return ignoredFiles
     */
    public static List<String> getIgnoredFiles()
    {
        return ignoredFiles;
    }
            
}
