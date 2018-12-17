/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import java.io.File;
import java.util.List;
import java.util.Vector;
/**
 * This class is really the proper entrance point for the rest of the program, all the heavy lifting will be done here
 * 
 * @author rplea
 */
public class Driver 
{
    
    private static Settings mySettings;
    private static SSH mySSH;
    private static SFTP mySFTP;
    private static boolean isConnect;
    
    public Driver()
    {
        mySettings = new Settings();
        mySSH = new SSH(mySettings);
        isConnect = mySSH.connect();
        mySFTP = new SFTP(mySSH);
    }
    
    public static boolean connect()
    {
        return mySSH.connect();
    }
    
    public static boolean disconnect()
    {
        return mySSH.disconnect();
    }
    
    public static void exec(String command)
    {
        mySSH.exec(command);
    }
    
    public static Vector updateFiles()
    {
        if(isConnected())
            return mySFTP.updateFiles();
        else 
            return null;
    }
    
    public static boolean sendFiles(List<File> selectFiles)
    {
        return mySFTP.sendFiles(selectFiles);
    }
    
    public static boolean deleteFiles(List<File> selectFiles)
    {
        return mySFTP.deleteFiles(selectFiles);
    }
    
    public static boolean isConnected()
    {
        return mySSH.isConnected();
    }
    
    public static void reboot()
    {
        exec("sudo /reboot.sh");
        disconnect(); //cleanly exit even if the remote server is already off
        mySSH = new SSH(mySettings); //all previous channels are unusable after reboot so need to generate a clean slate
        mySFTP = new SFTP(mySSH); //with a new SSH generated the old object in the previous SFTP will be useless
    }
}
