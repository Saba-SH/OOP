package com.example.part2;

import java.util.List;

public interface ProductDAO {
    public List<Product> list();

    public Product getProductById(String id);
}
