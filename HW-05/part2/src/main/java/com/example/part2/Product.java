package com.example.part2;

public class Product {
    private String id;
    private String name;
    private String imagefile;
    private double price;

    public Product(String id, String name, String imagefile, double price) {
        this.id = id;
        this.name = name;
        this.imagefile = imagefile;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagefile() {
        return imagefile;
    }

    public double getPrice() {
        return price;
    }
}
