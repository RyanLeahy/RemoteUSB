/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


/**
 * Class handles the SSH portion such as executing commands and establishing a connection and establishing credentials with the host
 * 
 * @author rplea
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
     * Function reads settings data taken from config file and attempts to establish a connection with raspberry pi.
     * 
     * @return connect
     */
    public boolean connect()
    {
        boolean connect = true; //returns if the program was able to connect
        
        try
        {
            mySession = myClient.getSession(myUsername, myAddress, myPort);
            mySession.setPassword(myPassword);
            java.util.Properties config = new java.util.Properties(); 
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
            mySession.setConfig(config);
            mySession.connect();
            myChannel = createChannel("exec");
            
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
     * Function runs command passed in a string
     * 
     * @param command 
     */
    public void exec(String command)
    {
        try
        {
            ((ChannelExec)myChannel).setCommand(command);
            myChannel.connect();
            myChannel.disconnect();
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
        exec("sudo mount -v -o offset=1048576 -t vfat /piusb.iso /mnt/usb");
    }
    
    /*
        Function unmounts the correct partition so is to not corrupt the partition
    */
    private void unmount()
    {
        exec("sudo umount /piusb.iso");
    }
    
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
        
        unmount();
        
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
