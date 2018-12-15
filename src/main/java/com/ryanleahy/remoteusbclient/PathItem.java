/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanleahy.remoteusbclient;

//Java imports
import java.io.File;
import java.util.ArrayList;

//JavaFX imports
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 *  Custom cell factory for UI ListView with a delete button.
 *
 * @author   Kevin Cruse from USB-Backup
 * @version  1.0
 */
public class PathItem extends ListCell<String>
{
    private HBox container;
    private Pane pane;
    private Label label;
    private Button remButton;

    /**
     *  Initialize list cell.
     */
    public PathItem()
    {
        super();

        container = new HBox();
        pane = new Pane();
        label = new Label("");
        remButton = new Button("X");

        container.getChildren().addAll(label, pane, remButton);
        HBox.setHgrow(pane, Priority.ALWAYS);
        remButton.setOnAction(this::delete);
    }
    
    /**
     *  Response to delete button being pressed.
     * @param event
     */
    public void delete(ActionEvent event)
    {
        ArrayList<File> file = new ArrayList<>();
        file.add(new File(getItem()));
        
        //Remove files from raspberry pi
        if(file.isEmpty() != true)
            Driver.deleteFiles(file);
        
        getListView().getItems().remove(getItem());
    }

    /**
     *  Update routine for cell.
     */
    @Override
    protected void updateItem(String item, boolean empty)
    {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        
        if (item != null && !empty)
        {
            label.setText(item);
            setGraphic(container);
        }
    }
}