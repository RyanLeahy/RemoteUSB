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
        mySFTPChannel = mySSH.createChannel("sftp");
        
        
        try
        {
            mySFTPChannel.connect(); //open connection
            workingList = ((ChannelSftp)mySFTPChannel).ls("/mnt/usb/"); //grab list of files currently in directory
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
        
        mySFTPChannel.disconnect(); //close connection
        
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
        mySFTPChannel = mySSH.createChannel("sftp");
        
        try
        {
            mySFTPChannel.connect(5000); //open connection
            ((ChannelSftp)mySFTPChannel).cd("/mnt/usb"); //move to the storage directory of the pi
            
            for(File currentFile : selectedFiles) //iterate through selected file
            {
                ((ChannelSftp)mySFTPChannel).put(currentFile.getAbsolutePath(), currentFile.getName()); //copy the file from local target and paste it in the directory
            }
            
        }
        catch(JSchException | SftpException e)
        {
            e.printStackTrace(); //something went wrong, print stack trace
            mySFTPChannel.disconnect(); //close connection
            
            return filesAdded;
        }
        
        mySFTPChannel.disconnect(); //all went well time to close the connection
        
        filesAdded = true;
        
        return filesAdded;
    }
    
    public boolean deleteFiles(List<File> selectedFiles)
    {
        boolean filesDeleted = false;
        mySFTPChannel = mySSH.createChannel("sftp");
        
        try
        {
            mySFTPChannel.connect(5000); //open connection
            ((ChannelSftp)mySFTPChannel).cd("/mnt/usb"); //move to the storage directory of the pi
            
            for(File currentFile : selectedFiles) //iterate through files selected for deletion
            {
                ((ChannelSftp)mySFTPChannel).rm(currentFile.getName());
            }
        }
        catch(JSchException | SftpException e)
        {
            e.printStackTrace(); //something went wrong, print stack trace, debug only
            mySFTPChannel.disconnect(); //close connection
            
            return filesDeleted;
        }
        
        mySFTPChannel.disconnect(); //all went well time to close the connection
        
        filesDeleted = true;
        
        return filesDeleted;         
    }
}
