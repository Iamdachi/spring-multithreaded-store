package com.dachi.spring.multithreadedstore.service;

import com.dachi.spring.multithreadedstore.model.Order;
import com.dachi.spring.multithreadedstore.model.Warehouse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Service that processes orders from a queue and updates the warehouse.
 * Orders are processed asynchronously and stored in a list of processed orders.
 */
@Service
public class OrderProcessor {

    private final BlockingQueue<Order> queue;
    private final List<Order> processedOrders = Collections.synchronizedList(new ArrayList<>());
    private final Warehouse warehouse;

    /**
     * Constructor injecting the warehouse and order queue.
     *
     * @param warehouse the warehouse to process orders
     * @param queue     the queue of incoming orders
     */
    public OrderProcessor(Warehouse warehouse, BlockingQueue<Order> queue) {
        this.warehouse = warehouse;
        this.queue = queue;
    }

    /**
     * Starts processing orders asynchronously.
     * Continuously takes orders from the queue and processes them in the warehouse.
     * Successfully processed orders are added to the processedOrders list.
     */
    @Async
    public void startProcessing() {
        while (true) {
            try {
                Order order = queue.take();
                if (this.warehouse.process(order)) {
                    processedOrders.add(order);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Returns the list of successfully processed orders.
     *
     * @return synchronized list of processed orders
     */
    public List<Order> getProcessedOrders() {
        return processedOrders;
    }
}