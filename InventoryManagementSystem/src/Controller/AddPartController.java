package Controller;

import Model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable
{
    @FXML
    private RadioButton inhouse_RB;
    @FXML
    private RadioButton outsourced_RB;
    @FXML
    private Label partCatagory_Label;
    @FXML
    private TextField id_TF;
    @FXML
    private TextField name_TF;
    @FXML
    private TextField inv_TF;
    @FXML
    private TextField price_TF;
    @FXML
    private TextField max_TF;
    @FXML
    private TextField min_TF;
    @FXML
    private TextField company_TF;

    private Inventory inventory;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.inventory = MainScreenController.getInventory();
        ObservableList<Part> partsList = inventory.getAllParts();

        if(partsList.isEmpty() || partsList==null)
        {
            id_TF.setText("1");
        }
        else
        {
            Part part = Collections.max(partsList, Comparator.comparing(s -> s.getId()));
            int newId = part.getId() + 1;
            id_TF.setText(String.valueOf(newId));
        }
    }

    @FXML
    public void onRadioButtonClick(ActionEvent event)
    {
        if(inhouse_RB.isSelected())
        {
            partCatagory_Label.setText("Machine ID");
            company_TF.setPromptText("Machine ID");
        }
        if(outsourced_RB.isSelected())
        {
            partCatagory_Label.setText("Company Name");
            company_TF.setPromptText("Company Name");
        }
    }

    @FXML
    public void onSaveAddPart(ActionEvent event) throws IOException
    {
        String errorLog = "Please fix below errors before proceeding:\n";
        boolean errorFlag = false;

        try
        {
            if(Integer.parseInt(inv_TF.getText())<Integer.parseInt(min_TF.getText()))
            {
                errorLog = errorLog + "\u2022 Inventory is below minimum allowance\n";
                errorFlag = true;
            }
            if(Integer.parseInt(inv_TF.getText()) > Integer.parseInt(max_TF.getText()))
            {
                errorLog = errorLog + "\u2022 Inventory exceeds maximum allowance\n";
                errorFlag = true;
            }
            if(Integer.parseInt(min_TF.getText()) > Integer.parseInt(max_TF.getText()))
            {
                errorLog = errorLog + "\u2022 Minimum value exceeds Maximum value\n";
                errorFlag = true;
            }

            if(errorFlag)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setContentText(errorLog);
                alert.showAndWait();
                return;
            }

            if (inhouse_RB.isSelected())
            {
                inventory.addPart(new InHouse(Integer.parseInt(id_TF.getText()), name_TF.getText(), Double.parseDouble(price_TF.getText()),
                        Integer.parseInt(inv_TF.getText()), Integer.parseInt(min_TF.getText()), Integer.parseInt(max_TF.getText()), Integer.parseInt(company_TF.getText())));
            }
            if (outsourced_RB.isSelected())
            {
                inventory.addPart(new OutSourced(Integer.parseInt(id_TF.getText()), name_TF.getText(), Double.parseDouble(price_TF.getText()),
                        Integer.parseInt(inv_TF.getText()), Integer.parseInt(min_TF.getText()), Integer.parseInt(max_TF.getText()), company_TF.getText()));
            }

            Parent mainParent = FXMLLoader.load(getClass().getResource("../View/MainScreen.fxml"));
            Scene mainScene = new Scene(mainParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        }
        catch(NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("Please enter a valid value for each Text Field");
            alert.showAndWait();
        }
    }

    @FXML
    public void onCancelAddPart(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any changes made will not be saved.");
        alert.setTitle("Do you really want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            Parent mainParent = FXMLLoader.load(getClass().getResource("../View/MainScreen.fxml"));
            Scene mainScene = new Scene(mainParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        }
    }

}




