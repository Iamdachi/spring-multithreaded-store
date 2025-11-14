package com.dachi.spring.multithreadedstore.service;

import com.dachi.spring.multithreadedstore.model.Order;
import com.dachi.spring.multithreadedstore.model.Product;
import com.dachi.spring.multithreadedstore.model.Warehouse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service that generates orders and puts them into a queue for processing.
 * Orders are created asynchronously and contain random products from the warehouse.
 */
@Service
public class OrderGenerator {

    private final Warehouse warehouse;
    private final BlockingQueue<Order> queue;
    private final List<Product> products;

    /**
     * Constructor injecting the warehouse, order queue, and available products.
     *
     * @param warehouse the warehouse
     * @param queue     the queue to place generated orders
     * @param products  the list of available products
     */
    public OrderGenerator(Warehouse warehouse, BlockingQueue<Order> queue, List<Product> products) {
        this.warehouse = warehouse;
        this.queue = queue;
        this.products = products;
    }

    /**
     * Generates a specified number of orders asynchronously.
     * Each order contains one random product and is added to the queue.
     *
     * @param totalOrders number of orders to generate
     */
    @Async
    public void generateOrders(int totalOrders) {
        for (int i = 0; i < totalOrders; i++) {
            Product product = products.get(ThreadLocalRandom.current().nextInt(products.size()));
            Order order = new Order();
            order.add(product, 1);
            queue.add(order);
        }
    }
}

