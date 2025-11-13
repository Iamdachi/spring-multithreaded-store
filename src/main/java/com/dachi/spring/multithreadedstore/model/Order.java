package com.dachi.spring.multithreadedstore.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an order containing products and their quantities.
 */
public class Order {

    private final Map<Product, Integer> items;

    /**
     * Creates a regular empty order.
     */
    public Order() {
        this.items = new HashMap<>();
    }

    /**
     * Adds a product and its quantity to the order.
     *
     * @param product the product to add
     * @param quantity the quantity of the product
     * @throws IllegalArgumentException if quantity is negative
     */
    public void add(Product product, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        items.put(product, quantity);
    }

    /**
     * Returns an unmodifiable view of the items in this order.
     *
     * @return map of products to their quantities
     */
    public Map<Product, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

}