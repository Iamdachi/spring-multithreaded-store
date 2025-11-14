package com.dachi.spring.multithreadedstore;

import com.dachi.spring.multithreadedstore.model.Order;
import com.dachi.spring.multithreadedstore.model.Product;
import com.dachi.spring.multithreadedstore.model.Warehouse;
import com.dachi.spring.multithreadedstore.service.OrderGenerator;
import com.dachi.spring.multithreadedstore.service.OrderProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Spring configuration class for defining application beans.
 * Provides beans for products, order queue, warehouse, and services.
 */
@Configuration
public class StoreConfig {

    /**
     * Provides a list of available products in the store.
     *
     * @return list of products
     */
    @Bean
    public List<Product> products() {
        return List.of(
                new Product("Phone", 800),
                new Product("Laptop", 1500),
                new Product("Headphones", 100),
                new Product("Xbox", 900),
                new Product("Keyboard", 150)
        );
    }

    /**
     * Provides a blocking queue for orders to be processed.
     *
     * @return order queue
     */
    @Bean
    public BlockingQueue<Order> orderQueue() {
        return new LinkedBlockingQueue<>();
    }

    /**
     * Provides the warehouse instance initialized with products.
     *
     * @param products list of products
     * @return warehouse
     */
    @Bean
    public Warehouse warehouse(List<Product> products) {
        return new Warehouse(products);
    }

    /**
     * Provides the order generator service.
     *
     * @param warehouse   the warehouse
     * @param orderQueue  the queue to place orders
     * @param products    list of products
     * @return order generator
     */
    @Bean
    public OrderGenerator orderGenerator(Warehouse warehouse,
                                         BlockingQueue<Order> orderQueue,
                                         List<Product> products) {
        return new OrderGenerator(warehouse, orderQueue, products);
    }

    /**
     * Provides the order processor service.
     *
     * @param warehouse  the warehouse
     * @param orderQueue the queue of orders
     * @return order processor
     */
    @Bean
    public OrderProcessor orderProcessor(Warehouse warehouse,
                                         BlockingQueue<Order> orderQueue) {
        return new OrderProcessor(warehouse, orderQueue);
    }
}
