package com.dachi.spring.multithreadedstore.service;

import com.dachi.spring.multithreadedstore.model.Order;
import com.dachi.spring.multithreadedstore.model.Product;
import com.dachi.spring.multithreadedstore.model.Warehouse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderGenerator {

    private final Warehouse warehouse;
    private final BlockingQueue<Order> queue;
    private final List<Product> products;

    public OrderGenerator(Warehouse warehouse, BlockingQueue<Order> queue, List<Product> products) {
        this.warehouse = warehouse;
        this.queue = queue;
        this.products = products;
    }

    @Async
    public CompletableFuture<Void> generateOrders(int totalOrders) {
        for (int i = 0; i < totalOrders; i++) {
            Product product = products.get(ThreadLocalRandom.current().nextInt(products.size()));
            Order order = new Order();
            order.add(product, 1);
            queue.add(order);
        }
        return CompletableFuture.completedFuture(null);
    }
}

