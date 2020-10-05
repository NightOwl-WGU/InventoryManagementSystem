package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import javafx.collections.FXCollections;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable
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

    private Stage stage;
    private Parent scene;

    private Inventory inventory;
    private Part modifiedPart;
    private int index;
    ObservableList<Part> partsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.inventory = MainScreenController.getInventory();
        this.partsList = inventory.getAllParts();
    }

    public void setPart(Part modifiedPart)
    {
        this.modifiedPart = modifiedPart;
        this.index = partsList.indexOf(modifiedPart);

        id_TF.setText(String.valueOf(modifiedPart.getId()));
        name_TF.setText(String.valueOf(modifiedPart.getName()));
        inv_TF.setText(String.valueOf(modifiedPart.getStock()));
        price_TF.setText(String.valueOf(modifiedPart.getPrice()));
        max_TF.setText(String.valueOf(modifiedPart.getMax()));
        min_TF.setText(String.valueOf(modifiedPart.getMin()));

        if(modifiedPart instanceof InHouse)
        {
            inhouse_RB.setSelected(true);
            partCatagory_Label.setText("Machine ID");
            company_TF.setText(String.valueOf(((InHouse) modifiedPart).getMachineId()));
        }
        else if(modifiedPart instanceof OutSourced)
        {
            outsourced_RB.setSelected(true);
            partCatagory_Label.setText("Company Name");
            company_TF.setText(String.valueOf(((OutSourced) modifiedPart).getCompanyName()));
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
        Part newPart;
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
                newPart = new InHouse(Integer.parseInt(id_TF.getText()), name_TF.getText(), Double.parseDouble(price_TF.getText()),
                        Integer.parseInt(inv_TF.getText()), Integer.parseInt(min_TF.getText()), Integer.parseInt(max_TF.getText()), Integer.parseInt(company_TF.getText()));
                inventory.updatePart(index, newPart);
            }
            if (outsourced_RB.isSelected())
            {
                newPart = new OutSourced(Integer.parseInt(id_TF.getText()), name_TF.getText(), Double.parseDouble(price_TF.getText()),
                        Integer.parseInt(inv_TF.getText()), Integer.parseInt(min_TF.getText()), Integer.parseInt(max_TF.getText()), company_TF.getText());
                inventory.updatePart(index, newPart);
            }

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../View/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
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
