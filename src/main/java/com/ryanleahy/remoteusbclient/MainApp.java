package com.ryanleahy.remoteusbclient;

import java.io.IOException;
import static java.lang.System.exit;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application 
{
    private static UI myUI;
    private Stage myStage;
    private Driver myDriver;
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    /**
     * Sets the UI being used by the program
     * 
     * @param ui the UI object
     */
    public static void setUI(UI ui)
    {
        myUI = ui;
    }
    
    /**
     * Initial entrance point of the program, sets up UI and the Driver which acts as the intermediary between the classes
     * @param stage the Stage object
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception 
    {
        myStage = stage;
        myDriver = new Driver();
        
        try
        {
            initStage();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            
            exit(1);
        }
        
        stage.show();
    }
    
    /**
     * overrides original stop function so when you press the red x it disconnects and unmounts beforehand
     */
    @Override
    public void stop()
    {
        Driver.disconnect();
    }
    
    /*
      Creates the UI object and loads all beginning UI stuff
    */
    private void initStage() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        myStage.setTitle("Remote USB");
        myStage.setScene(scene);
        myStage.setResizable(false);
    }

}
