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
        Vector<ChannelSftp.LsEntry> list = null;
        try
        {
            mySFTPChannel.connect();
            list = ((ChannelSftp)mySFTPChannel).ls("/mnt/usb/");
            for(ChannelSftp.LsEntry entry : list)
                System.out.println(entry.getFilename());
            mySFTPChannel.disconnect();
        }
        catch(JSchException | SftpException e)
        {
            e.printStackTrace();
            
            return null; //something went wrong
        }
        
        return list;
    }
    
    
}
