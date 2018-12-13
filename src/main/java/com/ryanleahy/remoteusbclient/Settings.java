/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

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
    
    /**
     * Constructor reads file and stores them in instance variables
     */
    public Settings()
    {
        myAddress = "192.168.1.131"; //TODO
        myUsername = "pi";
        myPassword = "roblox2401";
        myPort = 22;
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
