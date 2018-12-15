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
     * 
     * @param ui 
     */
    public static void setUI(UI ui)
    {
        myUI = ui;
    }
    
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
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        //Driver driver = FXMLLoader.getController()
        stage.setTitle("RemoteUSB");
        stage.setScene(scene);*/
        stage.show();
    }
    
    private void initStage() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Scene scene = new Scene(root);
        myStage.setScene(scene);
        myStage.setResizable(false);
    }

}
