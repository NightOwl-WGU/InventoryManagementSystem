package Model;

import javafx.collections.ObservableList;

public class Product
{
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    //constructor
    public Product(ObservableList<Part> associatedParts, int id, String name, double price, int stock, int min, int max)
    {
        this.associatedParts = associatedParts;
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    //methods
    public void addAssociatedPart(Part part)
    {
        this.associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatePart)
    {
        return false;
    }

    public ObservableList<Part> getAllAssociatedParts()
    {
        return this.associatedParts;
    }

    //setters
    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

    public void setMin(int min)
    {
        this.min = min;
    }

    public void setMax(int max)
    {
        this.max = max;
    }

    //getters
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public int getStock()
    {
        return stock;
    }

    public int getMin()
    {
        return min;
    }

    public int getMax()
    {
        return max;
    }
}
