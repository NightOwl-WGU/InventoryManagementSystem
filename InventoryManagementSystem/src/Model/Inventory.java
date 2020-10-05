package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory
{
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public void addPart(Part newPart)
    {
        if(newPart != null)
        {
            allParts.add(newPart);
        }
    }

    public void addProduct(Product newProduct)
    {
        if(newProduct != null)
        {
            allProducts.add(newProduct);
        }
    }

    public Part lookupPart(int partId)
    {
        Part part;

        for(Part p: allParts)
        {
            if(p.getId() == partId)
            {
                return p;
            }
        }
        return null;
    }

    public Product lookupProduct(int productId)
    {
        Product product;

        for(Product p: allProducts)
        {
            if(p.getId() == productId)
            {
                return p;
            }
        }
        return null;
    }

    public void updatePart(int index, Part selectedPart)
    {
        if(selectedPart != null)
        {
            allParts.set(index, selectedPart);
        }
    }

    public void updateProduct(int index, Product newProduct)
    {
        if(newProduct != null)
        {
            allProducts.set(index, newProduct);
        }
    }

    public boolean deletePart(Part selectedPart)
    {
        if(allParts.contains(selectedPart))
        {
            allParts.remove(selectedPart);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(Product selectedProduct)
    {
        if(allProducts.contains(selectedProduct))
        {
            allProducts.remove(selectedProduct);
            return true;
        }
        return false;
    }


    public ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    public ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }
}
