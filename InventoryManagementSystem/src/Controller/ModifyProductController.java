package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable
{
    @FXML
    private TableView<Part> partsTable1;
    @FXML
    private TableView<Part> partsTable2;
    @FXML
    private TableColumn partId1;
    @FXML
    private TableColumn partName1;
    @FXML
    private TableColumn partInv1;
    @FXML
    private TableColumn partPrice1;
    @FXML
    private TableColumn partId2;
    @FXML
    private TableColumn partName2;
    @FXML
    private TableColumn partInv2;
    @FXML
    private TableColumn partPrice2;
    @FXML
    private TextField id_TB;
    @FXML
    private TextField name_TB;
    @FXML
    private TextField inv_TB;
    @FXML
    private TextField price_TB;
    @FXML
    private TextField max_TB;
    @FXML
    private TextField min_TB;
    @FXML
    private TextField search_TB;
    @FXML
    public FilteredList<Part> filteredParts;

    private int index;
    private Product modifiedProduct;
    private Inventory inventory;
    private ObservableList<Part> allParts;
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    private ObservableList<Product> productList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.inventory = MainScreenController.getInventory();
        this.productList = inventory.getAllProducts();
    }

    public void setProduct(Product modifiedProduct)
    {
        this.modifiedProduct = modifiedProduct;
        this.index = productList.indexOf(modifiedProduct);
        this.productParts = modifiedProduct.getAllAssociatedParts();
        this.allParts = FXCollections.observableArrayList(inventory.getAllParts());

        for(int i=0; i < productParts.size(); i++)
        {
            for(int j=0; j < allParts.size(); j++)
            {
                if(productParts.get(i).getId() == allParts.get(j).getId())
                    allParts.remove(j);
            }
        }

        id_TB.setText(String.valueOf(modifiedProduct.getId()));
        name_TB.setText(String.valueOf(modifiedProduct.getName()));
        inv_TB.setText(String.valueOf(modifiedProduct.getStock()));
        price_TB.setText(String.valueOf(modifiedProduct.getPrice()));
        max_TB.setText(String.valueOf(modifiedProduct.getMax()));
        min_TB.setText(String.valueOf(modifiedProduct.getMin()));

        filteredParts = new FilteredList<>(allParts, p -> true);

        partsTable1.setItems(filteredParts);
        partId1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice1.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable2.setItems(productParts);
        partId2.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice2.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    @FXML
    public void onAddButton(ActionEvent event)
    {
        Part pt = partsTable1.getSelectionModel().getSelectedItem();

        if(pt == null)
            return;

        productParts.add(pt);
        allParts.remove(pt);
    }

    @FXML
    public void onDeleteButton(ActionEvent event)
    {
        Part pt = partsTable2.getSelectionModel().getSelectedItem();

        if(pt == null)
            return;

        allParts.add(pt);
        productParts.remove(pt);
    }

    @FXML
    public void onCancelButton(ActionEvent event) throws IOException
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

    @FXML
    public void onSaveButton(ActionEvent event) throws IOException
    {
        Product newProduct;
        double productCost = 0;
        String errorLog = "Please fix below errors before proceeding:\n";
        boolean errorFlag = false;

        try
        {
            if(name_TB.getText().trim().equals("") || name_TB.getText().trim().isBlank())
            {
                errorLog = errorLog + "\u2022 No product name entered\n";
                errorFlag = true;
            }
            if(Integer.parseInt(inv_TB.getText())<Integer.parseInt(min_TB.getText()))
            {
                errorLog = errorLog + "\u2022 Inventory is below minimum allowance\n";
                errorFlag = true;
            }
            if(Integer.parseInt(inv_TB.getText()) > Integer.parseInt(max_TB.getText()))
            {
                errorLog = errorLog + "\u2022 Inventory exceeds maximum allowance\n";
                errorFlag = true;
            }
            if(Integer.parseInt(min_TB.getText()) > Integer.parseInt(max_TB.getText()))
            {
                errorLog = errorLog + "\u2022 Minimum value exceeds Maximum value\n";
                errorFlag = true;
            }
            if(productParts.size() <= 0)
            {
                errorLog = errorLog + "\u2022 No parts selected\n";
                errorFlag = true;
            }

            for(Part p: productParts)
            {
                productCost += p.getPrice();
            }

            if(Double.parseDouble(price_TB.getText().trim()) <= productCost)
            {
                errorLog = errorLog + "\u2022 Product price must exceed total costs of parts: " + productCost + "\n";
                errorFlag = true;
            }

            if(errorFlag)
            {
                System.out.println(productParts.size());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setContentText(errorLog);
                alert.showAndWait();
                return;
            }

            newProduct = new Product(productParts, Integer.parseInt(id_TB.getText().trim()), name_TB.getText().trim(), Double.parseDouble(price_TB.getText().trim()),
                    Integer.parseInt(inv_TB.getText().trim()), Integer.parseInt(min_TB.getText().trim()), Integer.parseInt(max_TB.getText().trim()));

            inventory.updateProduct(index, newProduct);

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
    public void onSearchButton(ActionEvent event)
    {
        String criteria = search_TB.getText();

        filteredParts.setPredicate(part-> {
            if(criteria == null || criteria.isEmpty())
            {
                return true;
            }
            if(part.getName().equals(criteria) || Integer.toString(part.getId()).equals(criteria))
            {
                return true;
            }
            return false;
        });

        SortedList<Part> sortedParts = new SortedList<>(filteredParts);

        sortedParts.comparatorProperty().bind(partsTable1.comparatorProperty());
        partsTable1.setItems(sortedParts);
    }
}
