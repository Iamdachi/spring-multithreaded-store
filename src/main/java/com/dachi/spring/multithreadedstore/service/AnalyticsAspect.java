package com.dachi.spring.multithreadedstore.service;


import com.dachi.spring.multithreadedstore.model.Warehouse;
import com.dachi.spring.multithreadedstore.model.Order;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class AnalyticsAspect {

    private final AtomicLong totalOrders = new AtomicLong();
    private final AtomicLong totalProfit = new AtomicLong();
    private final Map<String, AtomicLong> productCounts = new ConcurrentHashMap<>();

    @AfterReturning(pointcut = "execution(* com.dachi.spring.multithreadedstore.model.Warehouse.process(..)) && args(order)", returning = "result")
    public void afterProcess(Order order, boolean result) {
        if (!result) return;
        totalOrders.incrementAndGet();
        order.getItems().forEach((product, qty) -> {
            totalProfit.addAndGet((long)(product.price() * qty));
            productCounts.computeIfAbsent(product.name(), k -> new AtomicLong()).addAndGet(qty);
        });
    }

    public long getTotalOrders() {
        return totalOrders.get();
    }

    public long getTotalProfit() {
        return totalProfit.get();
    }

    public List<String> getTop3Products() {
        return productCounts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }
}

