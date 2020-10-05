package Controller;

import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable
{
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn partId;
    @FXML
    private TableColumn partName;
    @FXML
    private TableColumn partInventoryLevel;
    @FXML
    private TableColumn partPrice;
    @FXML
    private TableColumn productId;
    @FXML
    private TableColumn productName;
    @FXML
    private TableColumn productInventoryLevel;
    @FXML
    private TableColumn productPrice;
    @FXML
    private TextField searchPart_TB;
    @FXML
    private TextField searchProduct_TB;
    @FXML
    public ObservableList<Part> allParts = FXCollections.observableArrayList();
    @FXML
    public FilteredList<Part> filteredParts;
    @FXML
    public ObservableList<Product> allProducts = FXCollections.observableArrayList();
    @FXML
    public FilteredList<Product> filteredProducts;

    private Stage stage;
    public static Inventory inventory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        inventory = new Inventory();
        allParts = inventory.getAllParts();
        filteredParts = new FilteredList<>(allParts, p -> true);

        partsTable.setItems(filteredParts);
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        allProducts = inventory.getAllProducts();
        filteredProducts = new FilteredList<>(allProducts, p -> true);

        productsTable.setItems(filteredProducts);
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    public static Inventory getInventory()
    {
        return inventory;
    }


    @FXML
    public void onAddPart(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/AddPart.fxml"));
        loader.load();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onAddProduct(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/AddProduct.fxml"));
        loader.load();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onModifyPart(ActionEvent event) throws IOException
    {
        Part pt = partsTable.getSelectionModel().getSelectedItem();

        if(pt == null)
            return;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/ModifyPart.fxml"));
        loader.load();
        ModifyPartController mpc = loader.getController();
        mpc.setPart(pt);
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onModifyProduct(ActionEvent event) throws IOException
    {
        Product product = productsTable.getSelectionModel().getSelectedItem();

        if(product == null)
            return;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/ModifyProduct.fxml"));
        loader.load();
        ModifyProductController mpc = loader.getController();
        mpc.setProduct(product);
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void onDeletePart(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Part?");
        alert.setTitle("Deleting is forever!!!");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            Part pt = partsTable.getSelectionModel().getSelectedItem();
            String toUpdate = "Products affected and may need to be updated:\n";

            if (pt == null)
                return;

            for(Product product: inventory.getAllProducts())
            {
                if(product.getAllAssociatedParts().contains(pt))
                {
                    toUpdate = toUpdate + "\u2022 " + product.getName() + "\n";
                }
            }

            for(Product p: inventory.getAllProducts())
                p.getAllAssociatedParts().remove(pt);

            inventory.deletePart(pt);

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, toUpdate);
            alert2.setTitle("Products Affected");
            alert2.showAndWait();
        }
    }

    @FXML
    public void onDeleteProduct(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Product?");
        alert.setTitle("Deleting is forever!!!");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            Product product = productsTable.getSelectionModel().getSelectedItem();

            if (product == null)
                return;

            inventory.deleteProduct(product);
        }
    }

    @FXML
    public void onSearchPart(ActionEvent event)
    {
        String criteria = searchPart_TB.getText();

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

        sortedParts.comparatorProperty().bind(partsTable.comparatorProperty());
        partsTable.setItems(sortedParts);
    }

    @FXML
    public void onSearchProduct(ActionEvent event)
    {
        String criteria = searchProduct_TB.getText();

        filteredProducts.setPredicate(product-> {
            if(criteria == null || criteria.isEmpty())
            {
                return true;
            }
            if(product.getName().equals(criteria) || Integer.toString(product.getId()).equals(criteria))
            {
                return true;
            }
            return false;
        });

        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);

        sortedProducts.comparatorProperty().bind(productsTable.comparatorProperty());
        productsTable.setItems(sortedProducts);
    }

    @FXML
    public void onExitButton(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to go?");
        alert.setTitle("Do you really want to leave?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            Platform.exit();
        }
    }

}
