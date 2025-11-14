package com.dachi.spring.multithreadedstore;

import com.dachi.spring.multithreadedstore.service.AnalyticsAspect;
import com.dachi.spring.multithreadedstore.service.OrderGenerator;
import com.dachi.spring.multithreadedstore.service.OrderProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * Main Spring Boot application for the multithreaded store.
 * Initializes the application context, starts order generation and processing,
 * and prints basic analytics after a delay.
 */
@SpringBootApplication
@EnableAsync
public class SpringMultithreadedStoreApplication implements ApplicationRunner {

    private final ApplicationContext ctx;

    /**
     * Constructor injecting the Spring application context.
     *
     * @param ctx the application context
     */
    public SpringMultithreadedStoreApplication(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Main entry point to start the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringMultithreadedStoreApplication.class, args);
    }

    /**
     * Runs after the Spring context is initialized.
     * Starts order processing and generation, then prints analytics.
     *
     * @param args application arguments
     * @throws Exception if interrupted during sleep
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        OrderGenerator generator = ctx.getBean(OrderGenerator.class);
        OrderProcessor processor = ctx.getBean(OrderProcessor.class);
        AnalyticsAspect analytics = ctx.getBean(AnalyticsAspect.class);

        processor.startProcessing();
        generator.generateOrders(50);

        Thread.sleep(15000);

        System.out.println("Processed orders: " + processor.getProcessedOrders().size());
        System.out.println("Total orders: " + analytics.getTotalOrders());
        System.out.println("Total profit: " + analytics.getTotalProfit());
        System.out.println("Top 3 products: " + analytics.getTop3Products());
    }
}
