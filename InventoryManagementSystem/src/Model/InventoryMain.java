package Model;

import Controller.MainScreenController;
import Controller.ModifyPartController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryMain extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Program (Justin Ross)");
        primaryStage.setScene(new Scene(root, 926, 372));
        primaryStage.show();

        //test data - parts
        MainScreenController.inventory.addPart(new InHouse(1, "part1", 123.00, 2, 2,5,321));
        MainScreenController.inventory.addPart(new InHouse(2, "part2", 254.00, 5, 2,5,323));
        MainScreenController.inventory.addPart(new OutSourced(3, "part3", 24.00, 4, 2,6, "Mary"));
        MainScreenController.inventory.addPart(new OutSourced(4, "part4", 2709.00, 3, 2,6, "Vincent"));
        MainScreenController.inventory.addPart(new InHouse(5, "part5", 123.00, 2, 2,5,321));
        MainScreenController.inventory.addPart(new InHouse(6, "part6", 254.00, 5, 2,5,323));
        MainScreenController.inventory.addPart(new OutSourced(7, "part7", 24.00, 4, 2,6, "Justin"));
        MainScreenController.inventory.addPart(new OutSourced(8, "part8", 2709.00, 3, 2,6, "Rusty"));
        //test data - products
        ObservableList<Part> testAllParts = FXCollections.observableArrayList(MainScreenController.inventory.getAllParts());
        ObservableList<Part> testPartLoader1 = FXCollections.observableArrayList();
        ObservableList<Part> testPartLoader2 = FXCollections.observableArrayList();


        //set products
        testPartLoader1.add(testAllParts.get(0));
        testPartLoader1.add(testAllParts.get(1));
        MainScreenController.inventory.addProduct(new Product(testPartLoader1, 1, "product 1", 145.23, 3, 1, 8));

        testPartLoader2.add(testAllParts.get(2));
        testPartLoader2.add(testAllParts.get(3));
        MainScreenController.inventory.addProduct(new Product(testPartLoader2, 2, "product 2", 99.99, 4, 1, 6));

        MainScreenController.inventory.addProduct(new Product(testPartLoader1, 3, "product 3", 145.23, 3, 1, 8));
        MainScreenController.inventory.addProduct(new Product(testPartLoader1, 4, "product 4", 145.23, 3, 1, 8));
        MainScreenController.inventory.addProduct(new Product(testPartLoader2, 5, "product 5", 99.99, 4, 1, 6));
        MainScreenController.inventory.addProduct(new Product(testPartLoader2, 6, "product 6", 99.99, 4, 1, 6));
    }


    public static void main(String[] args)
    {
        launch(args);
    }

}


