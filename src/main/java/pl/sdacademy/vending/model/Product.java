package pl.sdacademy.vending.model;

/**
 * Simple class that represents product that can be bought.
 */
public class Product {
    private String name;

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
