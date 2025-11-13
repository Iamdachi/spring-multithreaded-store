package com.dachi.spring.multithreadedstore.service;

import com.dachi.spring.multithreadedstore.model.Order;
import com.dachi.spring.multithreadedstore.model.Warehouse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
public class OrderProcessor {

    private final BlockingQueue<Order> queue;
    private final List<Order> processedOrders = Collections.synchronizedList(new ArrayList<>());
    private final Warehouse warehouse;

    public OrderProcessor(Warehouse warehouse, BlockingQueue<Order> queue) {
        this.warehouse = warehouse;
        this.queue = queue;
    }


    @Async
    public void startProcessing() {
        while (true) {
            try {
                Order order = queue.take();
                // process order
                if (this.warehouse.process(order)) {                // this call goes through Spring proxy
                    processedOrders.add(order);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public List<Order> getProcessedOrders() {
        return processedOrders;
    }
}
