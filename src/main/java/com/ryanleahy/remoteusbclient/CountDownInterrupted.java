/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread to handle telling the user that they already pressed the reboot button
 * 
 * @author rplea
 */
public class CountDownInterrupted implements Runnable 
{

    @Override
    public void run()
    {
        //UI.setStatus("Already Rebooting"); //update UI to tell user they've already pressed the reboot button
        
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(CountDownInterrupted.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        UI.setRebootPresses(UI.getRebootPresses() - 1); //set number of presses back to normal to tell the UI the event has been handled.
    }
    
}
