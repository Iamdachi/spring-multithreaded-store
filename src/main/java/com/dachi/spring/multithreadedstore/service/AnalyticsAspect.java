package com.dachi.spring.multithreadedstore.service;


import com.dachi.spring.multithreadedstore.model.Order;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aspect for collecting analytics on orders processed in the warehouse.
 * Tracks total orders, total profit, and counts per product.
 */
@Aspect
@Component
public class AnalyticsAspect {

    private final AtomicLong totalOrders = new AtomicLong();
    private final AtomicLong totalProfit = new AtomicLong();
    private final Map<String, AtomicLong> productCounts = new ConcurrentHashMap<>();

    /**
     * Advice executed after Warehouse.process() returns.
     * Updates total orders, total profit, and product counts if order was processed successfully.
     *
     * @param order  the processed order
     * @param result true if order was successfully processed
     */
    @AfterReturning(pointcut = "execution(* com.dachi.spring.multithreadedstore.model.Warehouse.process(..)) && args(order)", returning = "result")
    public void afterProcess(Order order, boolean result) {
        if (!result) return;
        totalOrders.incrementAndGet();
        order.getItems().forEach((product, qty) -> {
            totalProfit.addAndGet((long)(product.price() * qty));
            productCounts.computeIfAbsent(product.name(), k -> new AtomicLong()).addAndGet(qty);
        });
    }

    /**
     * Returns the total number of processed orders.
     */
    public long getTotalOrders() {
        return totalOrders.get();
    }

    /**
     * Returns the total profit from processed orders.
     */
    public long getTotalProfit() {
        return totalProfit.get();
    }

    /**
     * Returns a list of top 3 products by quantity sold.
     */
    public List<String> getTop3Products() {
        return productCounts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }
}

