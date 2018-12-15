/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Class handles all file transfer interactions and file explorer functionality
 * @author rplea
 */
public class SFTP
{
    private SSH mySSH;
    private Channel mySFTPChannel;
    
    public SFTP(SSH ssh)
    {
        mySSH = ssh;
        mySFTPChannel = mySSH.createChannel("sftp");
    }
    
    /**
     * Function returns an arraylist storing all the files in the storage drive so the UI can use it to provide a UI for deleting files and seeing what's in there
     * 
     * @return FileList
     */
    public Vector updateFiles()
    {
        Vector<ChannelSftp.LsEntry> workingList = null;
        Vector<ChannelSftp.LsEntry> list = new Vector<ChannelSftp.LsEntry>();
        
        
        try
        {
            mySFTPChannel.connect(); //open connection
            workingList = ((ChannelSftp)mySFTPChannel).ls("/mnt/usb/"); //grab list of files currently in directory
            mySFTPChannel.disconnect(); //close connection
        }
        catch(JSchException | SftpException e)
        {
            e.printStackTrace();
            mySFTPChannel.disconnect(); //close just in case something goes wrong
            
            return null; //something went wrong
        }
        
        //now we need to remove the junk stuff that aren't files
        for(ChannelSftp.LsEntry entry : workingList)
            if(!(entry.getFilename().equals(".") || entry.getFilename().equals("..") || entry.getFilename().equals("System Volume Information") || entry.getFilename().equals("$RECYCLE.BIN")))
                list.add(entry);
        
        return list;
    }
    
    
    /**
     * Function receives a list of files that want to be added to the remote server, takes in a list of files and puts them on the remote server.
     * Boolean being returned notifies if everything went correctly
     * 
     * @param selectedFiles
     * @return filesAdded
     */
    public boolean sendFiles(List<File> selectedFiles)
    {
        boolean filesAdded = false;
        
        try
        {
            mySFTPChannel.connect(); //open connection
            ((ChannelSftp)mySFTPChannel).cd("/home/pi/Downloads"); //move to the storage directory of the pi
            
            for(File currentFile : selectedFiles) //iterate through selected file
            {
                ((ChannelSftp)mySFTPChannel).put(currentFile.getAbsolutePath(), currentFile.getName()); //copy the file from local target and paste it in the directory
                Driver.exec("sudo cp /home/pi/Downloads/" + currentFile.getName() + " /mnt/usb/"); //had a permission problem so i put the file somewhere I could then copy it
                Driver.exec("sudo rm -f /home/pi/Downloads/" + currentFile.getName()); //delete the copied file
            }
            
        }
        catch(JSchException | SftpException e)
        {
            e.printStackTrace(); //something went wrong, print stack trace
            mySFTPChannel.disconnect(); //close connection
            
            return filesAdded;
        }
        
        mySFTPChannel.disconnect(); //all went well time to close the connection
        
        return !filesAdded; //change filesadded to true without changing variable
    }
}