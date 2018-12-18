package com.ryanleahy.remoteusbclient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


/**
 * Class handles the SSH portion such as executing commands and establishing a connection and establishing credentials with the host
 * 
 * @author Ryan Leahy
 */
public class SSH 
{
    private Settings mySettings;
    private String myAddress;
    private String myUsername;
    private String myPassword;
    private int myPort;
    private JSch myClient;
    private Session mySession;
    private Channel myChannel;
    
    public SSH(Settings settings)
    {
        mySettings = settings;
        myAddress = mySettings.getAddress();
        myUsername = mySettings.getUsername();
        myPassword = mySettings.getPassword();
        myPort = mySettings.getPort();
        myClient = new JSch();
        mySession = null;
        myChannel = null;
    }
    
    /**
     * Method reads settings data taken from config file and attempts to establish a connection with raspberry pi and returns a true boolean if it was able to establish a connection
     * 
     * @return connect
     */
    public boolean connect()
    {
        boolean connect = true; //returns if the program was able to connect
        
        try
        {
            mySession = myClient.getSession(myUsername, myAddress, myPort); //initiate session
            mySession.setPassword(myPassword); //pass the password
            java.util.Properties config = new java.util.Properties(); //check certain settings for connection parameters such as 
            config.put("StrictHostKeyChecking", "no"); //strictly checking the keys match, this reduces security but the scope of the project is not so concerned about the security
            config.put("PreferredAuthentications", "password"); //letting it know you want it to authenticate with a password
            mySession.setConfig(config);
            mySession.connect(5000); //try to connect and stop after 5 seconds
            myChannel = createChannel("exec"); //create a channel used for executing commands
            
        }
        catch(JSchException e)
        {
            connect = false;
            e.printStackTrace();
            
            return connect;
        }
        
        mount(); //mount the partition before anymore work is done
        
        return connect;
    }
    
    public boolean isConnected()
    {
        return mySession.isConnected();
    }
    
    /**
     * Method runs command passed in a string
     * 
     * @param command is a string holding a terminal command
     */
    public void exec(String command)
    {
        try
        {
            ((ChannelExec)myChannel).setCommand(command); 
            myChannel.connect(); //connecting runs the command
            myChannel.disconnect(); //close connection because the command was run
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }   
    }
    
    /*
        Function manages mounting the correct partitions
    */
    private void mount()
    {
        exec("sudo mount -v -o offset=1048576 -t vfat /piusb.iso /mnt/usb"); //the offset is very specific for my USB, if anyone actually cares about this project and complains i'll fix it
    }
    
    /*
        Function unmounts the correct partition so is to not corrupt the partition
    */
    private void unmount()
    {
        exec("sudo umount /piusb.iso");
    }
    
    /**
     * Function creates a Channel that follows the spec given in the String such as "exec" and passes back the channel
     * 
     * @param typeOf is a String holding what kind of channel it is
     * @return newChannel
     */
    public Channel createChannel(String typeOf)
    {
        try
        {
            return mySession.openChannel(typeOf);
        }
        catch (JSchException e)
        {
            e.printStackTrace();
            
            return null; //something went wrong so return NOTHING
        }
    }
    
    /**
     * Function disconnects the connection with the raspberry pi
     * 
     * @return disconnect 
     */
    public boolean disconnect()
    {
        boolean disconnect = true;
        
        unmount(); //super important to make sure you don't corrupt the USB portion
        
        try
        {
            mySession.disconnect();
        }
        catch(Exception e)
        {
            disconnect = false;
        }
        
        return disconnect;
    }
}
