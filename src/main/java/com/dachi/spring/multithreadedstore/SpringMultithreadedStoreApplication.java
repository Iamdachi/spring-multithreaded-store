package com.dachi.spring.multithreadedstore;

import com.dachi.spring.multithreadedstore.model.Order;
import com.dachi.spring.multithreadedstore.model.Product;
import com.dachi.spring.multithreadedstore.model.Warehouse;
import com.dachi.spring.multithreadedstore.service.AnalyticsAspect;
import com.dachi.spring.multithreadedstore.service.OrderGenerator;
import com.dachi.spring.multithreadedstore.service.OrderProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication
@EnableAsync
public class SpringMultithreadedStoreApplication implements ApplicationRunner {

    private final ApplicationContext ctx;

    public SpringMultithreadedStoreApplication(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringMultithreadedStoreApplication.class, args);
    }

    @Bean
    public List<Product> products() {
        return List.of(
                new Product("Phone", 800),
                new Product("Laptop", 1500),
                new Product("Headphones", 100)
        );
    }

    @Bean
    public BlockingQueue<Order> orderQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public Warehouse warehouse(List<Product> products) {
        return new Warehouse(products);
    }

    @Bean
    public OrderGenerator orderGenerator(Warehouse warehouse, BlockingQueue<Order> orderQueue, List<Product> products) {
        return new OrderGenerator(warehouse, orderQueue, products);
    }

    @Bean
    public OrderProcessor orderProcessor(Warehouse warehouse, BlockingQueue<Order> orderQueue) {
        return new OrderProcessor(warehouse, orderQueue);
    }

    @Bean
    public AnalyticsAspect analyticsAspect() {
        return new AnalyticsAspect();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        OrderGenerator generator = ctx.getBean(OrderGenerator.class);
        OrderProcessor processor = ctx.getBean(OrderProcessor.class);
        AnalyticsAspect analytics = ctx.getBean(AnalyticsAspect.class);

        processor.startProcessing();
        generator.generateOrders(50);

        Thread.sleep(5000);

        System.out.println("Processed orders: " + processor.getProcessedOrders().size());
        System.out.println("Total orders: " + analytics.getTotalOrders());
        System.out.println("Total profit: " + analytics.getTotalProfit());
        System.out.println("Top 3 products: " + analytics.getTop3Products());
    }
}
