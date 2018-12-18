package com.ryanleahy.remoteusbclient;

import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * This class is really the proper entrance point for the rest of the program, all the heavy lifting will be done here. Most interactions class interactions route through here
 * 
 * @author Ryan Leahy
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
    
    /**
     * Method tells the SSH class to attempt to connect, it returns a boolean to tell if it was successful or not in its endeavors.
     * 
     * @return connect
     */
    public static boolean connect()
    {
        return mySSH.connect();
    }
    
    /**
     * Method tells the SSH class to attempt to disconnect, returns boolean on how successful it was
     * 
     * @return disconnect
     */
    public static boolean disconnect()
    {
        return mySSH.disconnect();
    }
    
    /**
     * Method passes a String holding a terminal command for the SSH to pass to the USB
     * 
     * @param command is a String holding a terminal command
     */
    public static void exec(String command)
    {
        mySSH.exec(command);
    }
    
    /**
     * Method tells SFTP to return a Vector holding all the file names located in the USB directory in a Channelsftp.lsEntry object
     * for more information on Channelsftp.lsEntry please consult the JSCH java docs
     * 
     * @return filesList
     */
    public static Vector updateFiles()
    {
        if(isConnected())
            return mySFTP.updateFiles();
        else 
            return null;
    }
    
    /**
     * Method passes a List with File objects to SFTP for it to upload the files to the USB. sends back a boolean on how successful it was
     * 
     * @param selectFiles is a List of File objects
     * @return filesSent
     */
    public static boolean sendFiles(List<File> selectFiles)
    {
        return mySFTP.sendFiles(selectFiles);
    }
    
    /**
     * Method passes a List of File objects to SFTP for it to delete from the USB. sends back a boolean on how successful it was
     * 
     * @param selectFiles is a List of File objects
     * @return filesDeleted
     */
    public static boolean deleteFiles(List<File> selectFiles)
    {
        return mySFTP.deleteFiles(selectFiles);
    }
    
    /**
     * Method asks the SSH class if the SSH session is connected or not
     * 
     * @return isConnected
     */
    public static boolean isConnected()
    {
        return mySSH.isConnected();
    }
    
    /**
     * Method tells the USB to reboot and then creates a new SSH and SFTP object to reset them
     */
    public static void reboot()
    {
        exec("sudo /reboot.sh");
        disconnect(); //cleanly exit even if the remote server is already off
        mySSH = new SSH(mySettings); //all previous channels are unusable after reboot so need to generate a clean slate
        mySFTP = new SFTP(mySSH); //with a new SSH generated the old object in the previous SFTP will be useless
    }
}
