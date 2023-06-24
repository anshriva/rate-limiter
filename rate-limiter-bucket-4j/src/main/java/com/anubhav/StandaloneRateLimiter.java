package com.anubhav;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;


import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StandaloneRateLimiter {
    public static void main(String[] args) throws InterruptedException {
        Bucket bucket = Bucket.builder()
                // Bandwidth can be classic or simple
                // refill can be interval or greedy
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(20))))
                .build();


        var executorService = Executors.newScheduledThreadPool(10);
        executorService.scheduleAtFixedRate(() ->
                {
                    var probe = bucket.tryConsumeAndReturnRemaining(1);
                    System.out.println("isConsumed = "+ probe.isConsumed() +" probe = " + probe.toString());
                },
                0,
                1,
                TimeUnit.SECONDS);
        executorService.awaitTermination(100, TimeUnit.SECONDS);
        executorService.shutdown();
    }
}