/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import java.util.Vector;

/**
 * This class is really the proper entrance point for the rest of the program, all the heavy lifting will be done here
 * 
 * @author rplea
 */
public class Driver
{
    private Settings mySettings;
    private SSH mySSH;
    private SFTP mySFTP;
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
}
