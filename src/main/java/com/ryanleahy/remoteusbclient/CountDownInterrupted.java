package com.ryanleahy.remoteusbclient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread to handle telling the user that they already pressed the reboot button
 * 
 * @author Ryan Leahy
 */
public class CountDownInterrupted extends Thread
{

    private String myThreadName;
    private UI myUI;
    private Thread t;
    
    /**
     * Constructor constructs a thread and starts it
     * 
     * @param threadName is the String holding the name of the thread
     * @param ui is the UI used by the application
     */
    public CountDownInterrupted(String threadName, UI ui)
    {
        myThreadName = threadName;
        myUI = ui;
    }
    
    /**
     * Method starts the thread
     */
    @Override
    public void start()
    {
        if(t == null)
        {
            t = new Thread(this, myThreadName);
            t.start();
        }
    }
    
    /**
     * Method runs the threads code
     */
    @Override
    public void run()
    {
        myUI.setStatus("Already Rebooting"); //set ui message to let user know they already clicked the reset button
        
        /*
            This sleep has a specific purpose, in the event handler for the reboot button there is another thread counting down and updating the ui message every second
            but that message only updates if the amount of reboot button presses is equal to 1 so if this thread is created it's assumed that it equals more than one
            so by sleeping this thread for 2 seconds it allows it to display that message for two seconds because it can't put the amount of button presses back to normal.
        */
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(CountDownInterrupted.class.getName()).log(Level.SEVERE, null, ex); //still have no idea what this does, IDE generated it
        }
        
        UI.setRebootPresses(UI.getRebootPresses() - 1);
    }
    
}
