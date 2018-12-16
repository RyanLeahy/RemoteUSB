/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class reads a file and makes the settings accessible to the rest of the program
 * 
 * @author rplea
 */
public class Settings
{
    private String myAddress;
    private String myUsername;
    private String myPassword;
    private int myPort;
    private BufferedReader settingsFileReader;
    
    /**
     * Constructor reads file and stores them in instance variables
     */
    public Settings()
    {
        try
        {
            settingsFileReader = new BufferedReader(new FileReader(new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().toString() + "/settings.txt"));
        }
        catch (FileNotFoundException | URISyntaxException ex)
        {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {
            myAddress = settingsFileReader.readLine().substring(3);
            myUsername = settingsFileReader.readLine().substring(8);
            myPassword = settingsFileReader.readLine().substring(8);
            myPort = Integer.parseInt(settingsFileReader.readLine().substring(5));
        }
        catch (IOException ex)
        {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getAddress()
    {
        return myAddress;
    }
    
    public String getUsername()
    {
        return myUsername;
    }
    
    public String getPassword()
    {
        return myPassword;
    }
    
    public int getPort()
    {
        return myPort;
    }
}
